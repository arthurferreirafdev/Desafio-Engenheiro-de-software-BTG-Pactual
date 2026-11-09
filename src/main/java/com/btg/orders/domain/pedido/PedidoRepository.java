package com.btg.orders.domain.pedido;

/** Port de domínio — persistência do agregado Pedido. */
public interface PedidoRepository {

    boolean existePorCodigo(int codigoPedido);

    void salvar(Pedido pedido);
}
