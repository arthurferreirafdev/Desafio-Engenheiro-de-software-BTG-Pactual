package com.btg.orders.application.caso;

import com.btg.orders.application.ConsultaInvalidaException;
import com.btg.orders.application.dto.ContagemPedidosClienteResponse;
import com.btg.orders.application.dto.PaginaPedidosResponse;
import com.btg.orders.application.porta.ConsultaPedidos;

/** Caso de uso de leitura — quantidade e lista de pedidos por cliente (paginada). */
public class ConsultarPedidosClienteUseCase {

    private static final int TAMANHO_MAXIMO_PAGINA = 100;

    private final ConsultaPedidos consulta;

    public ConsultarPedidosClienteUseCase(ConsultaPedidos consulta) {
        this.consulta = consulta;
    }

    public ContagemPedidosClienteResponse contar(int codigoCliente) {
        validarCodigoCliente(codigoCliente);
        return new ContagemPedidosClienteResponse(codigoCliente, consulta.contarPedidosPorCliente(codigoCliente));
    }

    public PaginaPedidosResponse listar(int codigoCliente, int pagina, int tamanho) {
        validarCodigoCliente(codigoCliente);
        if (pagina < 1 || tamanho < 1 || tamanho > TAMANHO_MAXIMO_PAGINA) {
            throw new ConsultaInvalidaException(
                    "page deve ser >= 1 e size entre 1 e " + TAMANHO_MAXIMO_PAGINA);
        }
        return consulta.listarPedidosPorCliente(codigoCliente, pagina, tamanho);
    }

    private void validarCodigoCliente(int codigoCliente) {
        if (codigoCliente <= 0) {
            throw new ConsultaInvalidaException("codigoCliente deve ser positivo");
        }
    }
}
