package com.btg.orders.domain.produto;

import com.btg.orders.domain.DomainException;
import com.btg.orders.domain.valor.Preco;
import java.math.BigDecimal;

/** Entidade de catálogo — o código é gerado pela infraestrutura na persistência. */
public class Produto {

    private final int codigoProduto;
    private final String nome;
    private final Preco preco;

    public Produto(int codigoProduto, String nome, BigDecimal preco) {
        if (codigoProduto <= 0) {
            throw new DomainException("Código do produto deve ser positivo: " + codigoProduto);
        }
        if (nome == null || nome.isBlank()) {
            throw new DomainException("Nome do produto é obrigatório");
        }
        this.codigoProduto = codigoProduto;
        this.nome = nome.trim();
        this.preco = new Preco(preco);
    }

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public String getNome() {
        return nome;
    }

    public Preco getPreco() {
        return preco;
    }
}
