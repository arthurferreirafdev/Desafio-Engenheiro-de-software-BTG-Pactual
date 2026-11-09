package com.btg.orders.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_produto")
    private Integer codigoProduto;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    protected ProdutoEntity() {
    }

    public ProdutoEntity(String nome, BigDecimal preco) {
        this.nome = nome;
        this.preco = preco;
        this.criadoEm = LocalDateTime.now();
    }

    public Integer getCodigoProduto() {
        return codigoProduto;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
