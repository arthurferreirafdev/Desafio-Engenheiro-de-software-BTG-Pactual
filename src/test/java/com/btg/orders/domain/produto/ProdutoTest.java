package com.btg.orders.domain.produto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.btg.orders.domain.DomainException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProdutoTest {

    @Test
    @DisplayName("rejeita nome nulo, vazio ou em branco")
    void deveRejeitarNomeInvalido() {
        assertThrows(DomainException.class, () -> new Produto(1, null, new BigDecimal("1.00")));
        assertThrows(DomainException.class, () -> new Produto(1, "  ", new BigDecimal("1.00")));
        assertThrows(DomainException.class, () -> new Produto(1, "", new BigDecimal("1.00")));
    }

    @Test
    @DisplayName("rejeita código não-positivo")
    void deveRejeitarCodigoInvalido() {
        assertThrows(DomainException.class, () -> new Produto(0, "lápis", new BigDecimal("1.00")));
    }

    @Test
    @DisplayName("remove espaços das bordas do nome")
    void deveFazerTrimDoNome() {
        var produto = new Produto(1, "  lápis ", new BigDecimal("1.10"));

        assertEquals("lápis", produto.getNome());
        assertEquals(new BigDecimal("1.10"), produto.getPreco().valor());
    }
}
