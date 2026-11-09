package com.btg.orders.domain.valor;

import com.btg.orders.domain.DomainException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** Value object de dinheiro — não-negativo, sempre com escala 2 (HALF_UP). */
public record Preco(BigDecimal valor) {

    public Preco {
        if (valor == null) {
            throw new DomainException("Preço não pode ser nulo");
        }
        if (valor.signum() < 0) {
            throw new DomainException("Preço não pode ser negativo: " + valor);
        }
        valor = valor.setScale(2, RoundingMode.HALF_UP);
    }

    public static Preco de(BigDecimal valor) {
        return new Preco(valor);
    }

    public BigDecimal multiplicarPor(Quantidade quantidade) {
        return valor.multiply(BigDecimal.valueOf(quantidade.valor()));
    }
}
