package com.btg.orders.application.porta;

import java.util.function.Supplier;

/** Port transacional — delimita a unidade de trabalho de um caso de uso. */
public interface UnitOfWork {

    <T> T executar(Supplier<T> acao);
}
