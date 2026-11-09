package com.btg.orders.domain.valor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.btg.orders.domain.DomainException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PrecoTest {

    @Test
    @DisplayName("rejeita preço nulo e negativo")
    void deveRejeitarPrecosInvalidos() {
        assertThrows(DomainException.class, () -> new Preco(null));
        assertThrows(DomainException.class, () -> new Preco(new BigDecimal("-0.01")));
    }

    @Test
    @DisplayName("normaliza a escala para 2 casas (HALF_UP)")
    void deveNormalizarEscalaParaDuasCasas() {
        assertEquals(new BigDecimal("1.10"), new Preco(new BigDecimal("1.1")).valor());
        assertEquals(new BigDecimal("2.00"), new Preco(new BigDecimal("1.999")).valor());
        assertEquals(new BigDecimal("1.01"), new Preco(new BigDecimal("1.005")).valor());
    }

    @Test
    @DisplayName("multiplica preço por quantidade com precisão decimal")
    void deveMultiplicarPorQuantidade() {
        var total = new Preco(new BigDecimal("1.10")).multiplicarPor(new Quantidade(100));

        assertEquals(0, total.compareTo(new BigDecimal("110.00")));
    }
}
