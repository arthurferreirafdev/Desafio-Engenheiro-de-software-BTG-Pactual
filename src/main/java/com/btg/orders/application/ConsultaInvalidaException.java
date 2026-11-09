package com.btg.orders.application;

/** Parâmetro de consulta inválido — mapeado para HTTP 400 pela API. */
public class ConsultaInvalidaException extends RuntimeException {

    public ConsultaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
