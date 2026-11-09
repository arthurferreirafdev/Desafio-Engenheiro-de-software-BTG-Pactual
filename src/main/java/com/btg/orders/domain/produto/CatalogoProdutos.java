package com.btg.orders.domain.produto;

import java.math.BigDecimal;

/** Port de domínio — resolve (ou cria) o produto a partir do nome informado na mensagem. */
public interface CatalogoProdutos {

    Produto obterOuCriar(String nome, BigDecimal preco);
}
