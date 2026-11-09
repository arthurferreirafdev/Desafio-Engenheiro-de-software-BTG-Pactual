package com.btg.orders.infrastructure.persistence;

import com.btg.orders.application.dto.PaginaPedidosResponse;
import com.btg.orders.application.dto.PedidoResumoResponse;
import com.btg.orders.application.dto.TotalPedidoResponse;
import com.btg.orders.application.porta.ConsultaPedidos;
import com.btg.orders.infrastructure.persistence.repository.PedidoJpaRepository;
import java.math.RoundingMode;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/** Adapter do port de leitura — queries agregadas direto no PostgreSQL. */
@Component
public class ConsultaPedidosAdapter implements ConsultaPedidos {

    private final PedidoJpaRepository pedidosJpa;

    public ConsultaPedidosAdapter(PedidoJpaRepository pedidosJpa) {
        this.pedidosJpa = pedidosJpa;
    }

    @Override
    public Optional<TotalPedidoResponse> totalPorPedido(int codigoPedido) {
        return pedidosJpa.buscarTotalPorCodigo(codigoPedido)
                .map(total -> new TotalPedidoResponse(
                        total.getCodigoPedido(),
                        total.getCodigoCliente(),
                        total.getValorTotal().setScale(2, RoundingMode.HALF_UP)));
    }

    @Override
    public long contarPedidosPorCliente(int codigoCliente) {
        return pedidosJpa.countByCodigoCliente(codigoCliente);
    }

    @Override
    public PaginaPedidosResponse listarPedidosPorCliente(int codigoCliente, int pagina, int tamanho) {
        var resultado = pedidosJpa.buscarResumoPorCliente(codigoCliente, PageRequest.of(pagina - 1, tamanho));

        var pedidos = resultado.getContent().stream()
                .map(resumo -> new PedidoResumoResponse(
                        resumo.getCodigoPedido(),
                        resumo.getCriadoEm(),
                        resumo.getValorTotal().setScale(2, RoundingMode.HALF_UP),
                        resumo.getQuantidadeItens().intValue()))
                .toList();

        return new PaginaPedidosResponse(codigoCliente, pagina, tamanho, resultado.getTotalElements(), pedidos);
    }
}
