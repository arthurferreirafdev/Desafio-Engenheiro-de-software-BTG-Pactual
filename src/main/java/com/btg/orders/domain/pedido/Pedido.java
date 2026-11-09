package com.btg.orders.domain.pedido;

import com.btg.orders.domain.DomainException;
import com.btg.orders.domain.valor.Quantidade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Aggregate root — o valor total é sempre derivado dos itens, nunca armazenado. */
public class Pedido {

    private final int codigoPedido;
    private final int codigoCliente;
    private final LocalDateTime criadoEm;
    private final List<ItemPedido> itens;

    private Pedido(int codigoPedido, int codigoCliente, LocalDateTime criadoEm, List<ItemPedido> itens) {
        this.codigoPedido = codigoPedido;
        this.codigoCliente = codigoCliente;
        this.criadoEm = criadoEm;
        this.itens = itens;
    }

    public static Pedido novo(int codigoPedido, int codigoCliente, List<ItemPedido> itens) {
        if (codigoPedido <= 0) {
            throw new DomainException("Código do pedido deve ser positivo: " + codigoPedido);
        }
        if (codigoCliente <= 0) {
            throw new DomainException("Código do cliente deve ser positivo: " + codigoCliente);
        }
        List<ItemPedido> consolidados = consolidar(itens);
        if (consolidados.isEmpty()) {
            throw new DomainException("Pedido deve possuir ao menos um item");
        }
        return new Pedido(codigoPedido, codigoCliente, LocalDateTime.now(), consolidados);
    }

    /**
     * Consolida itens de um mesmo produto (soma quantidades, mantém o primeiro preço)
     * para respeitar a PK composta (codigo_pedido, codigo_produto).
     */
    private static List<ItemPedido> consolidar(List<ItemPedido> itens) {
        if (itens == null) {
            return List.of();
        }
        Map<Integer, ItemPedido> porProduto = new LinkedHashMap<>();
        for (ItemPedido item : itens) {
            if (item == null) {
                continue;
            }
            porProduto.merge(item.getCodigoProduto(), item, (existente, novo) -> new ItemPedido(
                    existente.getCodigoProduto(),
                    existente.getCodigoCliente(),
                    new Quantidade(existente.getQuantidade().valor() + novo.getQuantidade().valor()),
                    existente.getPreco()));
        }
        return List.copyOf(porProduto.values());
    }

    public BigDecimal valorTotal() {
        return itens.stream()
                .map(ItemPedido::valorTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public int getCodigoPedido() {
        return codigoPedido;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }
}
