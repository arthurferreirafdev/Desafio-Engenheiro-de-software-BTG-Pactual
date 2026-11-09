package com.btg.orders.domain.valor;

import com.btg.orders.domain.DomainException;

/** Value object de quantidade — inteiro estritamente positivo. */
public record Quantidade(int valor) {

    public Quantidade {
        if (valor <= 0) {
            throw new DomainException("Quantidade deve ser maior que zero: " + valor);
        }
    }
}
