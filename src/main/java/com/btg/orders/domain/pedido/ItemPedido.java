package com.btg.orders.domain.pedido;

import com.btg.orders.domain.DomainException;
import com.btg.orders.domain.valor.Preco;
import com.btg.orders.domain.valor.Quantidade;
import java.math.BigDecimal;

/** Item do agregado {@link Pedido} — preço histórico no momento do pedido. */
public class ItemPedido {

    private final int codigoProduto;
    private final int codigoCliente;
    private final Quantidade quantidade;
    private final Preco preco;

    public ItemPedido(int codigoProduto, int codigoCliente, Quantidade quantidade, Preco preco) {
        if (codigoProduto <= 0) {
            throw new DomainException("Código do produto deve ser positivo: " + codigoProduto);
        }
        if (codigoCliente <= 0) {
            throw new DomainException("Código do cliente deve ser positivo: " + codigoCliente);
        }
        if (quantidade == null) {
            throw new DomainException("Quantidade é obrigatória");
        }
        if (preco == null) {
            throw new DomainException("Preço é obrigatório");
        }
        this.codigoProduto = codigoProduto;
        this.codigoCliente = codigoCliente;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public Quantidade getQuantidade() {
        return quantidade;
    }

    public Preco getPreco() {
        return preco;
    }

    public BigDecimal valorTotalItem() {
        return preco.multiplicarPor(quantidade);
    }
}
