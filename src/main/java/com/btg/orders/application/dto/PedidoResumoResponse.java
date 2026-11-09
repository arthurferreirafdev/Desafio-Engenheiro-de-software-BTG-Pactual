package com.btg.orders.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Item da lista de pedidos por cliente. */
public record PedidoResumoResponse(int codigoPedido, LocalDateTime criadoEm, BigDecimal valorTotal,
                                   int quantidadeItens) {
}
