package com.btg.orders.application.caso;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.btg.orders.application.ConsultaInvalidaException;
import com.btg.orders.application.dto.PaginaPedidosResponse;
import com.btg.orders.application.suporte.FakeConsultaPedidos;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConsultarPedidosClienteUseCaseTest {

    private final FakeConsultaPedidos consulta = new FakeConsultaPedidos();
    private final ConsultarPedidosClienteUseCase caso = new ConsultarPedidosClienteUseCase(consulta);

    @Test
    @DisplayName("conta os pedidos do cliente")
    void deveContarPedidosDoCliente() {
        consulta.dadaContagem(1, 3);

        var resposta = caso.contar(1);

        assertEquals(3, resposta.quantidadePedidos());
        assertEquals(1, resposta.codigoCliente());
    }

    @Test
    @DisplayName("retorna zero para cliente sem pedidos")
    void deveRetornarZeroParaClienteSemPedidos() {
        assertEquals(0, caso.contar(42).quantidadePedidos());
    }

    @Test
    @DisplayName("lista pedidos paginados do cliente")
    void deveListarPedidosDoCliente() {
        consulta.dadaPagina(new PaginaPedidosResponse(1, 1, 10, 1, List.of()));

        var resposta = caso.listar(1, 1, 10);

        assertEquals(1, resposta.totalPedidos());
        assertEquals(1, resposta.pagina());
        assertEquals(10, resposta.tamanho());
    }

    @Test
    @DisplayName("rejeita paginação inválida")
    void deveRejeitarPaginacaoInvalida() {
        assertThrows(ConsultaInvalidaException.class, () -> caso.listar(1, 0, 10));
        assertThrows(ConsultaInvalidaException.class, () -> caso.listar(1, 1, 0));
        assertThrows(ConsultaInvalidaException.class, () -> caso.listar(1, 1, 101));
    }

    @Test
    @DisplayName("rejeita código de cliente não-positivo")
    void deveRejeitarCodigoClienteInvalido() {
        assertThrows(ConsultaInvalidaException.class, () -> caso.contar(0));
        assertThrows(ConsultaInvalidaException.class, () -> caso.listar(-1, 1, 10));
    }
}
