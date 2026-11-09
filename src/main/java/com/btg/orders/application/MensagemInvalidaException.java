package com.btg.orders.application;

/** Mensagem que viola o contrato esperado — deve ser descartada na DLQ, sem retry. */
public class MensagemInvalidaException extends RuntimeException {

    public MensagemInvalidaException(String mensagem) {
        super(mensagem);
    }

    public MensagemInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
