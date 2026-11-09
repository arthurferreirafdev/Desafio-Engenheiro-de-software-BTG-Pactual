package com.btg.orders.application.suporte;

import com.btg.orders.domain.produto.CatalogoProdutos;
import com.btg.orders.domain.produto.Produto;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FakeCatalogoProdutos implements CatalogoProdutos {

    public final Map<String, Produto> porNome = new HashMap<>();
    private int proximoCodigo = 1;

    @Override
    public Produto obterOuCriar(String nome, BigDecimal preco) {
        return porNome.computeIfAbsent(nome.trim().toLowerCase(),
                chave -> new Produto(proximoCodigo++, nome.trim(), preco));
    }
}
