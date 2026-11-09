package com.btg.orders.application.dto;

import java.math.BigDecimal;
import java.util.List;

/** Contrato da mensagem consumida da fila RabbitMQ (exatamente como no problem.md). */
public record PedidoMessage(int codigoPedido, int codigoCliente, List<ItemMessage> itens) {

    public record ItemMessage(String produto, int quantidade, BigDecimal preco) {
    }
}
