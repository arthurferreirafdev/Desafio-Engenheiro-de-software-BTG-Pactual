package com.btg.orders.application.caso;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.btg.orders.application.ConsultaInvalidaException;
import com.btg.orders.application.PedidoNaoEncontradoException;
import com.btg.orders.application.dto.TotalPedidoResponse;
import com.btg.orders.application.suporte.FakeConsultaPedidos;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConsultarValorTotalPedidoUseCaseTest {

    private final FakeConsultaPedidos consulta = new FakeConsultaPedidos();
    private final ConsultarValorTotalPedidoUseCase caso = new ConsultarValorTotalPedidoUseCase(consulta);

    @Test
    @DisplayName("retorna o valor total do pedido existente")
    void deveRetornarTotalDoPedido() {
        consulta.dadoTotal(new TotalPedidoResponse(1001, 1, new BigDecimal("120.00")));

        var resposta = caso.executar(1001);

        assertEquals(new BigDecimal("120.00"), resposta.valorTotal());
        assertEquals(1, resposta.codigoCliente());
    }

    @Test
    @DisplayName("lança PedidoNaoEncontrado quando o pedido não existe")
    void deveLancarQuandoPedidoNaoExiste() {
        assertThrows(PedidoNaoEncontradoException.class, () -> caso.executar(999999));
    }

    @Test
    @DisplayName("rejeita código de pedido não-positivo")
    void deveRejeitarCodigoInvalido() {
        assertThrows(ConsultaInvalidaException.class, () -> caso.executar(0));
    }
}
