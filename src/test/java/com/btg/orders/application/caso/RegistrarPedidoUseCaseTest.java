package com.btg.orders.application.caso;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.btg.orders.application.MensagemInvalidaException;
import com.btg.orders.application.dto.PedidoMessage;
import com.btg.orders.application.dto.ResultadoRegistro;
import com.btg.orders.application.suporte.FakeCatalogoProdutos;
import com.btg.orders.application.suporte.FakePedidoRepository;
import com.btg.orders.application.suporte.FakeUnitOfWork;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrarPedidoUseCaseTest {

    private final FakePedidoRepository pedidos = new FakePedidoRepository();
    private final FakeCatalogoProdutos catalogo = new FakeCatalogoProdutos();
    private final RegistrarPedidoUseCase caso =
            new RegistrarPedidoUseCase(pedidos, catalogo, new FakeUnitOfWork());

    private static PedidoMessage mensagemExemplo() {
        return new PedidoMessage(1001, 1, List.of(
                new PedidoMessage.ItemMessage("lápis", 100, new BigDecimal("1.10")),
                new PedidoMessage.ItemMessage("caderno", 10, new BigDecimal("1.00"))));
    }

    @Test
    @DisplayName("registra pedido novo, resolve produtos por nome e calcula total 120,00")
    void deveRegistrarPedidoNovo() {
        var resultado = caso.executar(mensagemExemplo());

        assertEquals(ResultadoRegistro.NOVO, resultado);
        assertEquals(1, pedidos.armazenados.size());
        assertEquals(new BigDecimal("120.00"), pedidos.armazenados.get(1001).valorTotal());
        assertEquals(2, catalogo.porNome.size());
    }

    @Test
    @DisplayName("retorna DUPLICADO e não persiste novamente quando o pedido já existe (idempotência)")
    void deveRetornarDuplicadoQuandoPedidoJaExiste() {
        caso.executar(mensagemExemplo());

        var resultado = caso.executar(mensagemExemplo());

        assertEquals(ResultadoRegistro.DUPLICADO, resultado);
        assertEquals(1, pedidos.armazenados.size());
    }

    @Test
    @DisplayName("consolida itens de um mesmo produto (mesmo nome, case-insensitive) somando quantidades")
    void deveConsolidarItensDeMesmoProduto() {
        var mensagem = new PedidoMessage(2002, 2, List.of(
                new PedidoMessage.ItemMessage("Lápis", 30, new BigDecimal("1.10")),
                new PedidoMessage.ItemMessage(" lápis ", 20, new BigDecimal("1.10"))));

        var resultado = caso.executar(mensagem);

        assertEquals(ResultadoRegistro.NOVO, resultado);
        var pedido = pedidos.armazenados.get(2002);
        assertEquals(1, pedido.getItens().size());
        assertEquals(50, pedido.getItens().get(0).getQuantidade().valor());
        assertEquals(new BigDecimal("55.00"), pedido.valorTotal());
        assertEquals(1, catalogo.porNome.size());
    }

    @Test
    @DisplayName("rejeita mensagem com itens vazio")
    void deveRejeitarPedidoSemItens() {
        var mensagem = new PedidoMessage(1001, 1, List.of());

        assertThrows(MensagemInvalidaException.class, () -> caso.executar(mensagem));
        assertTrue(pedidos.armazenados.isEmpty());
    }

    @Test
    @DisplayName("rejeita mensagem com preço negativo")
    void deveRejeitarPrecoNegativo() {
        var mensagem = new PedidoMessage(1001, 1, List.of(
                new PedidoMessage.ItemMessage("lápis", 10, new BigDecimal("-1.00"))));

        assertThrows(MensagemInvalidaException.class, () -> caso.executar(mensagem));
    }

    @Test
    @DisplayName("rejeita mensagem com preço nulo")
    void deveRejeitarPrecoNulo() {
        var mensagem = new PedidoMessage(1001, 1, List.of(
                new PedidoMessage.ItemMessage("lápis", 10, null)));

        assertThrows(MensagemInvalidaException.class, () -> caso.executar(mensagem));
    }

    @Test
    @DisplayName("rejeita mensagem com quantidade zero")
    void deveRejeitarQuantidadeZero() {
        var mensagem = new PedidoMessage(1001, 1, List.of(
                new PedidoMessage.ItemMessage("lápis", 0, new BigDecimal("1.00"))));

        assertThrows(MensagemInvalidaException.class, () -> caso.executar(mensagem));
    }

    @Test
    @DisplayName("rejeita mensagem com nome de produto ausente")
    void deveRejeitarProdutoSemNome() {
        var mensagem = new PedidoMessage(1001, 1, List.of(
                new PedidoMessage.ItemMessage("  ", 10, new BigDecimal("1.00"))));

        assertThrows(MensagemInvalidaException.class, () -> caso.executar(mensagem));
    }

    @Test
    @DisplayName("rejeita mensagem com códigos não-positivos")
    void deveRejeitarCodigosInvalidos() {
        assertThrows(MensagemInvalidaException.class,
                () -> caso.executar(new PedidoMessage(0, 1, List.of(
                        new PedidoMessage.ItemMessage("lápis", 10, new BigDecimal("1.00"))))));
        assertThrows(MensagemInvalidaException.class,
                () -> caso.executar(new PedidoMessage(1001, -1, List.of(
                        new PedidoMessage.ItemMessage("lápis", 10, new BigDecimal("1.00"))))));
    }

    @Test
    @DisplayName("rejeita mensagem nula")
    void deveRejeitarMensagemNula() {
        assertThrows(MensagemInvalidaException.class, () -> caso.executar(null));
    }
}
