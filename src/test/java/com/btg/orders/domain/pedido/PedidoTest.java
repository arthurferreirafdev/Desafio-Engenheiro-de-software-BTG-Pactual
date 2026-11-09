package com.btg.orders.domain.pedido;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.btg.orders.domain.DomainException;
import com.btg.orders.domain.valor.Preco;
import com.btg.orders.domain.valor.Quantidade;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PedidoTest {

    private static ItemPedido item(int codigoProduto, int quantidade, String preco) {
        return new ItemPedido(codigoProduto, 1, new Quantidade(quantidade), Preco.de(new BigDecimal(preco)));
    }

    @Test
    @DisplayName("calcula o valor total do pedido-exemplo do desafio (100 × 1,10 + 10 × 1,00 = 120,00)")
    void deveCalcularValorTotalDoPedidoExemplo() {
        var pedido = Pedido.novo(1001, 1, List.of(item(1, 100, "1.10"), item(2, 10, "1.00")));

        assertEquals(new BigDecimal("120.00"), pedido.valorTotal());
    }

    @Test
    @DisplayName("consolida itens de um mesmo produto somando as quantidades")
    void deveConsolidarItensDeMesmoProduto() {
        var pedido = Pedido.novo(1001, 1, List.of(item(1, 2, "3.00"), item(1, 3, "3.00")));

        assertEquals(1, pedido.getItens().size());
        assertEquals(5, pedido.getItens().get(0).getQuantidade().valor());
        assertEquals(new BigDecimal("15.00"), pedido.valorTotal());
    }

    @Test
    @DisplayName("rejeita pedido sem itens")
    void deveRejeitarPedidoSemItens() {
        assertThrows(DomainException.class, () -> Pedido.novo(1001, 1, List.of()));
        assertThrows(DomainException.class, () -> Pedido.novo(1001, 1, null));
    }

    @Test
    @DisplayName("rejeita código de pedido não-positivo")
    void deveRejeitarCodigoPedidoInvalido() {
        assertThrows(DomainException.class, () -> Pedido.novo(0, 1, List.of(item(1, 1, "1.00"))));
        assertThrows(DomainException.class, () -> Pedido.novo(-5, 1, List.of(item(1, 1, "1.00"))));
    }

    @Test
    @DisplayName("rejeita código de cliente não-positivo")
    void deveRejeitarCodigoClienteInvalido() {
        assertThrows(DomainException.class, () -> Pedido.novo(1001, 0, List.of(item(1, 1, "1.00"))));
    }

    @Test
    @DisplayName("permite preço zero e calcula total zero")
    void devePermitirPrecoZero() {
        var pedido = Pedido.novo(1001, 1, List.of(item(1, 5, "0.00")));

        assertEquals(new BigDecimal("0.00"), pedido.valorTotal());
    }
}
