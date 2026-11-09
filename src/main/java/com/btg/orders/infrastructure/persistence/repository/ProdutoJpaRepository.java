package com.btg.orders.infrastructure.persistence.repository;

import com.btg.orders.infrastructure.persistence.entity.ProdutoEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoJpaRepository extends JpaRepository<ProdutoEntity, Integer> {

    @Query("select p from ProdutoEntity p where lower(trim(p.nome)) = lower(trim(:nome))")
    Optional<ProdutoEntity> buscarPorNomeNormalizado(@Param("nome") String nome);
}
