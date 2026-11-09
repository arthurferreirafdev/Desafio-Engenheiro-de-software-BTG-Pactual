package com.btg.orders.infrastructure.persistence;

import com.btg.orders.domain.pedido.Pedido;
import com.btg.orders.domain.pedido.PedidoRepository;
import com.btg.orders.infrastructure.persistence.entity.ItemPedidoEntity;
import com.btg.orders.infrastructure.persistence.entity.PedidoEntity;
import com.btg.orders.infrastructure.persistence.repository.ItemPedidoJpaRepository;
import com.btg.orders.infrastructure.persistence.repository.PedidoJpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Adapter do port de domínio — persiste o agregado e seus itens em uma transação. */
@Repository
public class PedidoRepositoryAdapter implements PedidoRepository {

    private final PedidoJpaRepository pedidosJpa;
    private final ItemPedidoJpaRepository itensJpa;

    public PedidoRepositoryAdapter(PedidoJpaRepository pedidosJpa, ItemPedidoJpaRepository itensJpa) {
        this.pedidosJpa = pedidosJpa;
        this.itensJpa = itensJpa;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigoPedido) {
        return pedidosJpa.existsById(codigoPedido);
    }

    @Override
    @Transactional
    public void salvar(Pedido pedido) {
        var entidade = new PedidoEntity(pedido.getCodigoPedido(), pedido.getCodigoCliente(), pedido.getCriadoEm());
        pedidosJpa.saveAndFlush(entidade);

        List<ItemPedidoEntity> itens = pedido.getItens().stream()
                .map(item -> new ItemPedidoEntity(
                        pedido.getCodigoPedido(),
                        item.getCodigoProduto(),
                        item.getCodigoCliente(),
                        item.getPreco().valor(),
                        item.getQuantidade().valor()))
                .toList();
        itensJpa.saveAll(itens);
    }
}
