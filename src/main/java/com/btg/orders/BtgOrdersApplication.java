package com.btg.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Composition root — único ponto do sistema que conhece o Spring.
 * A classe fica no pacote raiz para que o component-scan padrão alcance
 * todas as camadas (api, application, domain, infrastructure).
 */
@SpringBootApplication
public class BtgOrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(BtgOrdersApplication.class, args);
    }
}
