package com.btg.orders.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class PedidoEntity {

    @Id
    @Column(name = "codigo_pedido")
    private Integer codigoPedido;

    @Column(name = "codigo_cliente", nullable = false)
    private Integer codigoCliente;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    protected PedidoEntity() {
    }

    public PedidoEntity(Integer codigoPedido, Integer codigoCliente, LocalDateTime criadoEm) {
        this.codigoPedido = codigoPedido;
        this.codigoCliente = codigoCliente;
        this.criadoEm = criadoEm;
    }

    public Integer getCodigoPedido() {
        return codigoPedido;
    }

    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
