package com.btg.orders.application.dto;

import java.math.BigDecimal;

/** Resposta de GET /api/pedidos/{codigoPedido}/total. */
public record TotalPedidoResponse(int codigoPedido, int codigoCliente, BigDecimal valorTotal) {
}
