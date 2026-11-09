package com.btg.orders.api.config;

import com.btg.orders.application.caso.ConsultarPedidosClienteUseCase;
import com.btg.orders.application.caso.ConsultarValorTotalPedidoUseCase;
import com.btg.orders.application.caso.RegistrarPedidoUseCase;
import com.btg.orders.application.porta.ConsultaPedidos;
import com.btg.orders.application.porta.UnitOfWork;
import com.btg.orders.domain.pedido.PedidoRepository;
import com.btg.orders.domain.produto.CatalogoProdutos;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Composition root dos casos de uso — mantém a camada de aplicação
 * livre de dependências do framework (DIP).
 */
@Configuration
public class CasosDeUsoConfig {

    @Bean
    public RegistrarPedidoUseCase registrarPedidoUseCase(PedidoRepository pedidos,
                                                         CatalogoProdutos catalogo,
                                                         UnitOfWork unitOfWork) {
        return new RegistrarPedidoUseCase(pedidos, catalogo, unitOfWork);
    }

    @Bean
    public ConsultarValorTotalPedidoUseCase consultarValorTotalPedidoUseCase(ConsultaPedidos consulta) {
        return new ConsultarValorTotalPedidoUseCase(consulta);
    }

    @Bean
    public ConsultarPedidosClienteUseCase consultarPedidosClienteUseCase(ConsultaPedidos consulta) {
        return new ConsultarPedidosClienteUseCase(consulta);
    }
}
