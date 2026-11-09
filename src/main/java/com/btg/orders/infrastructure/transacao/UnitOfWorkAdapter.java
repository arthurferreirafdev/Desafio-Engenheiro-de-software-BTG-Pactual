package com.btg.orders.infrastructure.transacao;

import com.btg.orders.application.porta.UnitOfWork;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/** Adapter do port transacional — delimita a transação com Spring. */
@Component
public class UnitOfWorkAdapter implements UnitOfWork {

    private final TransactionTemplate transacao;

    public UnitOfWorkAdapter(PlatformTransactionManager gerenciadorTransacao) {
        this.transacao = new TransactionTemplate(gerenciadorTransacao);
    }

    @Override
    public <T> T executar(Supplier<T> acao) {
        return transacao.execute(status -> acao.get());
    }
}
