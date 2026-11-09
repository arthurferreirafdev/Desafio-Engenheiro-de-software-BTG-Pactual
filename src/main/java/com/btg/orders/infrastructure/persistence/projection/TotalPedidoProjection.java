package com.btg.orders.infrastructure.persistence.projection;

import java.math.BigDecimal;

public interface TotalPedidoProjection {

    Integer getCodigoPedido();

    Integer getCodigoCliente();

    BigDecimal getValorTotal();
}
