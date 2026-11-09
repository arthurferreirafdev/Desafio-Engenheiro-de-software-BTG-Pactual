package com.btg.orders.application.suporte;

import com.btg.orders.domain.pedido.Pedido;
import com.btg.orders.domain.pedido.PedidoRepository;
import java.util.HashMap;
import java.util.Map;

public class FakePedidoRepository implements PedidoRepository {

    public final Map<Integer, Pedido> armazenados = new HashMap<>();

    @Override
    public boolean existePorCodigo(int codigoPedido) {
        return armazenados.containsKey(codigoPedido);
    }

    @Override
    public void salvar(Pedido pedido) {
        armazenados.put(pedido.getCodigoPedido(), pedido);
    }
}
