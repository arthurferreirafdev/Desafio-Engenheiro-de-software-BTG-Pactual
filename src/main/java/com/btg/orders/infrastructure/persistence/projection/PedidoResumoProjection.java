package com.btg.orders.infrastructure.persistence.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PedidoResumoProjection {

    Integer getCodigoPedido();

    LocalDateTime getCriadoEm();

    BigDecimal getValorTotal();

    Long getQuantidadeItens();
}
