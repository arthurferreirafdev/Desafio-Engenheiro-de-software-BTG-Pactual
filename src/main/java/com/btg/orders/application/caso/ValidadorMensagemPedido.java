package com.btg.orders.application.caso;

import com.btg.orders.application.MensagemInvalidaException;
import com.btg.orders.application.dto.PedidoMessage;
import java.util.List;

/** Validações antecipadas do contrato da mensagem (erros permanentes → DLQ). */
final class ValidadorMensagemPedido {

    private ValidadorMensagemPedido() {
    }

    static void validar(PedidoMessage mensagem) {
        if (mensagem.codigoPedido() <= 0) {
            throw new MensagemInvalidaException("codigoPedido deve ser positivo");
        }
        if (mensagem.codigoCliente() <= 0) {
            throw new MensagemInvalidaException("codigoCliente deve ser positivo");
        }
        if (mensagem.itens() == null || mensagem.itens().isEmpty()) {
            throw new MensagemInvalidaException("Pedido deve possuir ao menos um item");
        }
        for (PedidoMessage.ItemMessage item : mensagem.itens()) {
            validarItem(item);
        }
    }

    private static void validarItem(PedidoMessage.ItemMessage item) {
        if (item == null) {
            throw new MensagemInvalidaException("Item do pedido não pode ser nulo");
        }
        if (item.produto() == null || item.produto().isBlank()) {
            throw new MensagemInvalidaException("Nome do produto é obrigatório em todos os itens");
        }
        if (item.preco() == null || item.preco().signum() < 0) {
            throw new MensagemInvalidaException("Preço deve ser informado e não-negativo em todos os itens");
        }
        if (item.quantidade() <= 0) {
            throw new MensagemInvalidaException("Quantidade deve ser maior que zero em todos os itens");
        }
    }
}
