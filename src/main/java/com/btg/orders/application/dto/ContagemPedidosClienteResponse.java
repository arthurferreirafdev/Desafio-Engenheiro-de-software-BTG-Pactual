package com.btg.orders.application.dto;

/** Resposta de GET /api/clientes/{codigoCliente}/pedidos/quantidade. */
public record ContagemPedidosClienteResponse(int codigoCliente, long quantidadePedidos) {
}
