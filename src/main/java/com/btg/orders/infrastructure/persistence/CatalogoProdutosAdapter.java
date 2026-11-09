package com.btg.orders.infrastructure.persistence;

import com.btg.orders.domain.produto.CatalogoProdutos;
import com.btg.orders.domain.produto.Produto;
import com.btg.orders.infrastructure.persistence.entity.ProdutoEntity;
import com.btg.orders.infrastructure.persistence.repository.ProdutoJpaRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adapter do port de catálogo — find-or-create por nome normalizado
 * (trim + case-insensitive) e atualização do preço vigente do produto.
 */
@Repository
public class CatalogoProdutosAdapter implements CatalogoProdutos {

    private final ProdutoJpaRepository produtosJpa;

    public CatalogoProdutosAdapter(ProdutoJpaRepository produtosJpa) {
        this.produtosJpa = produtosJpa;
    }

    @Override
    @Transactional
    public Produto obterOuCriar(String nome, BigDecimal preco) {
        String nomeNormalizado = nome.trim();
        BigDecimal precoNormalizado = preco.setScale(2, RoundingMode.HALF_UP);

        return produtosJpa.buscarPorNomeNormalizado(nomeNormalizado)
                .map(entidade -> {
                    if (entidade.getPreco().compareTo(precoNormalizado) != 0) {
                        entidade.setPreco(precoNormalizado);
                        produtosJpa.saveAndFlush(entidade);
                    }
                    return new Produto(entidade.getCodigoProduto(), entidade.getNome(), entidade.getPreco());
                })
                .orElseGet(() -> {
                    var criada = produtosJpa.saveAndFlush(new ProdutoEntity(nomeNormalizado, precoNormalizado));
                    return new Produto(criada.getCodigoProduto(), criada.getNome(), criada.getPreco());
                });
    }
}
