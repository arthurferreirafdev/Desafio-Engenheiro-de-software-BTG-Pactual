#!/usr/bin/env bash
# Publica a mensagem-exemplo do problem.md na fila de pedidos via Management API do RabbitMQ.
# Uso: ./scripts/send-message.sh [arquivo.json]
set -euo pipefail

RABBIT_HOST="${RABBIT_HOST:-localhost}"
RABBIT_PORT="${RABBIT_PORT:-15672}"
RABBIT_USER="${RABBIT_USER:-guest}"
RABBIT_PASS="${RABBIT_PASS:-guest}"

MENSAGEM='{"codigoPedido":1001,"codigoCliente":1,"itens":[{"produto":"lápis","quantidade":100,"preco":1.10},{"produto":"caderno","quantidade":10,"preco":1.00}]}'

if [ "${1:-}" != "" ]; then
  MENSAGEM=$(cat "$1")
fi

PAYLOAD=$(printf '%s' "$MENSAGEM" | python3 -c 'import json,sys; print(json.dumps(sys.stdin.read()))')

RESPOSTA=$(curl -sS -u "$RABBIT_USER:$RABBIT_PASS" \
  -H "Content-Type: application/json" \
  -X POST "http://$RABBIT_HOST:$RABBIT_PORT/api/exchanges/%2f/pedidos.exchange/publish" \
  -d "{\"properties\":{\"content_type\":\"application/json\",\"delivery_mode\":2},\"routing_key\":\"pedido.criado\",\"payload\":$PAYLOAD,\"payload_encoding\":\"string\"}")

echo "Resposta do RabbitMQ: $RESPOSTA"
echo "Mensagem publicada:"
echo "$MENSAGEM"
