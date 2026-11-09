package com.btg.orders.application.caso;

import com.btg.orders.application.MensagemInvalidaException;
import com.btg.orders.application.dto.PedidoMessage;
import com.btg.orders.application.dto.ResultadoRegistro;
import com.btg.orders.application.porta.UnitOfWork;
import com.btg.orders.domain.DomainException;
import com.btg.orders.domain.pedido.ItemPedido;
import com.btg.orders.domain.pedido.Pedido;
import com.btg.orders.domain.pedido.PedidoRepository;
import com.btg.orders.domain.produto.CatalogoProdutos;
import com.btg.orders.domain.produto.Produto;
import com.btg.orders.domain.valor.Preco;
import com.btg.orders.domain.valor.Quantidade;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Caso de uso de escrita — registra o pedido vindo da fila de forma idempotente:
 * valida o contrato, resolve produtos por nome (find-or-create), persiste o agregado
 * dentro de uma única transação e confirma o ack apenas após o commit.
 */
public class RegistrarPedidoUseCase {

    private final PedidoRepository pedidos;
    private final CatalogoProdutos catalogo;
    private final UnitOfWork unitOfWork;

    public RegistrarPedidoUseCase(PedidoRepository pedidos, CatalogoProdutos catalogo, UnitOfWork unitOfWork) {
        this.pedidos = pedidos;
        this.catalogo = catalogo;
        this.unitOfWork = unitOfWork;
    }

    public ResultadoRegistro executar(PedidoMessage mensagem) {
        if (mensagem == null) {
            throw new MensagemInvalidaException("Mensagem nula");
        }
        ValidadorMensagemPedido.validar(mensagem);

        if (pedidos.existePorCodigo(mensagem.codigoPedido())) {
            return ResultadoRegistro.DUPLICADO;
        }

        try {
            return unitOfWork.executar(() -> registrar(mensagem));
        } catch (DomainException e) {
            throw new MensagemInvalidaException("Mensagem viola as invariantes de domínio: " + e.getMessage(), e);
        }
    }

    private ResultadoRegistro registrar(PedidoMessage mensagem) {
        List<ItemPedido> itens = new ArrayList<>();
        for (PedidoMessage.ItemMessage item : consolidarPorProduto(mensagem.itens()).values()) {
            Produto produto = catalogo.obterOuCriar(item.produto().trim(), item.preco());
            itens.add(new ItemPedido(
                    produto.getCodigoProduto(),
                    mensagem.codigoCliente(),
                    new Quantidade(item.quantidade()),
                    Preco.de(item.preco())));
        }
        Pedido pedido = Pedido.novo(mensagem.codigoPedido(), mensagem.codigoCliente(), itens);
        pedidos.salvar(pedido);
        return ResultadoRegistro.NOVO;
    }

    /**
     * Consolida itens de um mesmo produto (mesmo nome normalizado) somando quantidades,
     * pois a mensagem identifica produtos por nome e a PK composta exige unicidade
     * de (codigo_pedido, codigo_produto).
     */
    private Map<String, PedidoMessage.ItemMessage> consolidarPorProduto(List<PedidoMessage.ItemMessage> itens) {
        Map<String, PedidoMessage.ItemMessage> porNome = new LinkedHashMap<>();
        for (PedidoMessage.ItemMessage item : itens) {
            String chave = item.produto().trim().toLowerCase(Locale.ROOT);
            porNome.merge(chave, item, (a, b) -> new PedidoMessage.ItemMessage(
                    a.produto(),
                    a.quantidade() + b.quantidade(),
                    a.preco()));
        }
        return porNome;
    }
}
