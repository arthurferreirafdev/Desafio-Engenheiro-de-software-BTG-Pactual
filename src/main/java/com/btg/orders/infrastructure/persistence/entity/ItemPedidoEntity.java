package com.btg.orders.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
public class ItemPedidoEntity {

    @EmbeddedId
    private ItemPedidoId id;

    @Column(name = "codigo_cliente", nullable = false)
    private Integer codigoCliente;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    protected ItemPedidoEntity() {
    }

    public ItemPedidoEntity(int codigoPedido, int codigoProduto, int codigoCliente,
                            BigDecimal preco, int quantidade) {
        this.id = new ItemPedidoId(codigoPedido, codigoProduto);
        this.codigoCliente = codigoCliente;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public ItemPedidoId getId() {
        return id;
    }

    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }
}
