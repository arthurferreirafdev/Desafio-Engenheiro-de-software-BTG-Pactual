package com.btg.orders.infrastructure.persistence.repository;

import com.btg.orders.infrastructure.persistence.entity.PedidoEntity;
import com.btg.orders.infrastructure.persistence.projection.PedidoResumoProjection;
import com.btg.orders.infrastructure.persistence.projection.TotalPedidoProjection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Integer> {

    long countByCodigoCliente(int codigoCliente);

    @Query(value = """
            SELECT p.codigo_pedido AS "codigoPedido",
                   p.codigo_cliente AS "codigoCliente",
                   COALESCE(SUM(i.quantidade * i.preco), 0) AS "valorTotal"
            FROM pedido p
            LEFT JOIN item_pedido i ON i.codigo_pedido = p.codigo_pedido
            WHERE p.codigo_pedido = :codigoPedido
            GROUP BY p.codigo_pedido, p.codigo_cliente
            """, nativeQuery = true)
    Optional<TotalPedidoProjection> buscarTotalPorCodigo(@Param("codigoPedido") int codigoPedido);

    @Query(value = """
            SELECT p.codigo_pedido AS "codigoPedido",
                   p.criado_em AS "criadoEm",
                   COALESCE(SUM(i.quantidade * i.preco), 0) AS "valorTotal",
                   COUNT(i.codigo_pedido) AS "quantidadeItens"
            FROM pedido p
            LEFT JOIN item_pedido i ON i.codigo_pedido = p.codigo_pedido
            WHERE p.codigo_cliente = :codigoCliente
            GROUP BY p.codigo_pedido, p.criado_em
            ORDER BY p.codigo_pedido DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM pedido p
                    WHERE p.codigo_cliente = :codigoCliente
                    """,
            nativeQuery = true)
    Page<PedidoResumoProjection> buscarResumoPorCliente(@Param("codigoCliente") int codigoCliente,
                                                        Pageable pageable);
}
