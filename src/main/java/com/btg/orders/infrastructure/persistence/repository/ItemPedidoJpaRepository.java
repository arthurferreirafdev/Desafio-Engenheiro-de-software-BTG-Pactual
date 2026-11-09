package com.btg.orders.infrastructure.persistence.repository;

import com.btg.orders.infrastructure.persistence.entity.ItemPedidoEntity;
import com.btg.orders.infrastructure.persistence.entity.ItemPedidoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoJpaRepository extends JpaRepository<ItemPedidoEntity, ItemPedidoId> {
}
