package com.btg.orders.api.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI btgOrdersOpenApi() {
        return new OpenAPI().info(new Info()
                .title("BTG Orders API")
                .description("API de consulta de pedidos — Desafio Engenheiro de Software BTG Pactual")
                .version("1.0.0"));
    }
}
