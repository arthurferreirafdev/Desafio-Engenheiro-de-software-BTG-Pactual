package com.btg.orders.domain;

/** Exceção lançada quando uma invariante de domínio é violada. */
public class DomainException extends RuntimeException {

    public DomainException(String mensagem) {
        super(mensagem);
    }
}
