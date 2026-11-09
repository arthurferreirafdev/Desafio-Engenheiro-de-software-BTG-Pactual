package com.btg.orders.application;

/** Pedido consultado não existe — mapeado para HTTP 404 pela API. */
public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException(int codigoPedido) {
        super("Pedido não encontrado: " + codigoPedido);
    }
}
