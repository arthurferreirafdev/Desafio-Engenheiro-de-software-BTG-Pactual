package com.btg.orders.application.suporte;

import com.btg.orders.application.dto.ContagemPedidosClienteResponse;
import com.btg.orders.application.dto.PaginaPedidosResponse;
import com.btg.orders.application.dto.TotalPedidoResponse;
import com.btg.orders.application.porta.ConsultaPedidos;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeConsultaPedidos implements ConsultaPedidos {

    private final Map<Integer, TotalPedidoResponse> totais = new HashMap<>();
    private final Map<Integer, Long> contagens = new HashMap<>();
    private final Map<Integer, PaginaPedidosResponse> paginas = new HashMap<>();

    public void dadoTotal(TotalPedidoResponse resposta) {
        totais.put(resposta.codigoPedido(), resposta);
    }

    public void dadaContagem(int codigoCliente, long quantidade) {
        contagens.put(codigoCliente, quantidade);
    }

    public void dadaPagina(PaginaPedidosResponse resposta) {
        paginas.put(resposta.codigoCliente(), resposta);
    }

    @Override
    public Optional<TotalPedidoResponse> totalPorPedido(int codigoPedido) {
        return Optional.ofNullable(totais.get(codigoPedido));
    }

    @Override
    public long contarPedidosPorCliente(int codigoCliente) {
        return contagens.getOrDefault(codigoCliente, 0L);
    }

    @Override
    public PaginaPedidosResponse listarPedidosPorCliente(int codigoCliente, int pagina, int tamanho) {
        return paginas.getOrDefault(codigoCliente,
                new PaginaPedidosResponse(codigoCliente, pagina, tamanho, 0L, List.of()));
    }
}
