package com.btg.orders.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemPedidoId implements Serializable {

    @Column(name = "codigo_pedido")
    private Integer codigoPedido;

    @Column(name = "codigo_produto")
    private Integer codigoProduto;

    protected ItemPedidoId() {
    }

    public ItemPedidoId(Integer codigoPedido, Integer codigoProduto) {
        this.codigoPedido = codigoPedido;
        this.codigoProduto = codigoProduto;
    }

    public Integer getCodigoPedido() {
        return codigoPedido;
    }

    public Integer getCodigoProduto() {
        return codigoProduto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemPedidoId that)) {
            return false;
        }
        return Objects.equals(codigoPedido, that.codigoPedido)
                && Objects.equals(codigoProduto, that.codigoProduto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoPedido, codigoProduto);
    }
}
