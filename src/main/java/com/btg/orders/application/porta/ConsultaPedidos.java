package com.btg.orders.application.porta;

import com.btg.orders.application.dto.PaginaPedidosResponse;
import com.btg.orders.application.dto.TotalPedidoResponse;
import java.util.Optional;

/** Port de leitura (lado query do CQRS leve) — implementado pela infraestrutura. */
public interface ConsultaPedidos {

    Optional<TotalPedidoResponse> totalPorPedido(int codigoPedido);

    long contarPedidosPorCliente(int codigoCliente);

    PaginaPedidosResponse listarPedidosPorCliente(int codigoCliente, int pagina, int tamanho);
}
