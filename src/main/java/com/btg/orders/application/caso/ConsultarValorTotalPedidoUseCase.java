package com.btg.orders.application.caso;

import com.btg.orders.application.ConsultaInvalidaException;
import com.btg.orders.application.PedidoNaoEncontradoException;
import com.btg.orders.application.dto.TotalPedidoResponse;
import com.btg.orders.application.porta.ConsultaPedidos;

/** Caso de uso de leitura — valor total de um pedido. */
public class ConsultarValorTotalPedidoUseCase {

    private final ConsultaPedidos consulta;

    public ConsultarValorTotalPedidoUseCase(ConsultaPedidos consulta) {
        this.consulta = consulta;
    }

    public TotalPedidoResponse executar(int codigoPedido) {
        if (codigoPedido <= 0) {
            throw new ConsultaInvalidaException("codigoPedido deve ser positivo");
        }
        return consulta.totalPorPedido(codigoPedido)
                .orElseThrow(() -> new PedidoNaoEncontradoException(codigoPedido));
    }
}
