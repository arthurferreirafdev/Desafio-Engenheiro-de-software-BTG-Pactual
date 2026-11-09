package com.btg.orders.application.dto;

import java.util.List;

/** Resposta paginada de GET /api/clientes/{codigoCliente}/pedidos. */
public record PaginaPedidosResponse(int codigoCliente, int pagina, int tamanho, long totalPedidos,
                                     List<PedidoResumoResponse> pedidos) {
}
