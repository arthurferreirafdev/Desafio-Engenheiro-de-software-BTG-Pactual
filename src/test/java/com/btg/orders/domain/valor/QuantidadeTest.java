package com.btg.orders.domain.valor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.btg.orders.domain.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantidadeTest {

    @Test
    @DisplayName("rejeita quantidade zero e negativa")
    void deveRejeitarQuantidadesInvalidas() {
        assertThrows(DomainException.class, () -> new Quantidade(0));
        assertThrows(DomainException.class, () -> new Quantidade(-3));
    }

    @Test
    @DisplayName("aceita quantidade positiva")
    void deveAceitarQuantidadePositiva() {
        assertEquals(7, new Quantidade(7).valor());
    }
}
