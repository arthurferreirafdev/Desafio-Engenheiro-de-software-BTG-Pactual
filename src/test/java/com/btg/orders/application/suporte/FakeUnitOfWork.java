package com.btg.orders.application.suporte;

import com.btg.orders.application.porta.UnitOfWork;
import java.util.function.Supplier;

/** UnitOfWork fake que executa a ação direto, sem transação real. */
public class FakeUnitOfWork implements UnitOfWork {

    @Override
    public <T> T executar(Supplier<T> acao) {
        return acao.get();
    }
}
