package com.btg.orders.api.web;

import com.btg.orders.application.ConsultaInvalidaException;
import com.btg.orders.application.PedidoNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** Erros no formato ProblemDetails (RFC 7807). */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ProblemDetail pedidoNaoEncontrado(PedidoNaoEncontradoException e) {
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problema.setTitle("Pedido não encontrado");
        return problema;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail recursoInexistente(NoResourceFoundException e) {
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Recurso não encontrado");
        problema.setTitle("Não encontrado");
        return problema;
    }

    @ExceptionHandler({ConsultaInvalidaException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class})
    public ProblemDetail requisicaoInvalida(Exception e) {
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                e.getMessage() == null ? "Requisição inválida" : e.getMessage());
        problema.setTitle("Requisição inválida");
        return problema;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail erroNaoEsperado(Exception e) {
        log.error("Erro interno não esperado", e);
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno não esperado");
        problema.setTitle("Erro interno");
        return problema;
    }
}
