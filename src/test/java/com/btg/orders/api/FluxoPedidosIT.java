package com.btg.orders.api;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Fluxo completo: publicar mensagem na fila → consumer persiste no PostgreSQL →
 * consultas via API REST. Usa Postgres e RabbitMQ reais via Testcontainers.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FluxoPedidosIT {

    private static final String MENSAGEM_EXEMPLO = """
            {
              "codigoPedido": 1001,
              "codigoCliente": 1,
              "itens": [
                { "produto": "lápis",   "quantidade": 100, "preco": 1.10 },
                { "produto": "caderno", "quantidade": 10,  "preco": 1.00 }
              ]
            }
            """;

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("btg")
                    .withUsername("btg")
                    .withPassword("btg");

    @Container
    static final RabbitMQContainer RABBIT =
            new RabbitMQContainer("rabbitmq:3.13-management");

    @DynamicPropertySource
    static void registrarPropriedades(DynamicPropertyRegistry registro) {
        registro.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registro.add("spring.datasource.username", POSTGRES::getUsername);
        registro.add("spring.datasource.password", POSTGRES::getPassword);
        registro.add("spring.rabbitmq.host", RABBIT::getHost);
        registro.add("spring.rabbitmq.port", RABBIT::getAmqpPort);
    }

    @Autowired
    TestRestTemplate rest;

    @Autowired
    RabbitTemplate rabbit;

    private void publicar(String mensagem) {
        rabbit.convertAndSend("pedidos.exchange", "pedido.criado", mensagem);
    }

    @Test
    @Order(1)
    void deveProcessarMensagemDaFilaEPersistirPedidoComTotalCorreto() {
        publicar(MENSAGEM_EXEMPLO);

        await().atMost(ofSeconds(30)).untilAsserted(() -> {
            var resposta = rest.getForEntity("/api/pedidos/1001/total", String.class);
            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resposta.getBody()).contains("120.00");
        });
    }

    @Test
    @Order(2)
    void naoDeveDuplicarPedidoQuandoMensagemReenviada() {
        publicar(MENSAGEM_EXEMPLO);
        publicar(MENSAGEM_EXEMPLO);

        await().atMost(ofSeconds(30)).untilAsserted(() -> {
            var resposta = rest.getForEntity("/api/clientes/1/pedidos/quantidade", String.class);
            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resposta.getBody()).contains("\"quantidadePedidos\":1");
        });
    }

    @Test
    @Order(3)
    void deveListarPedidosDoClienteComValorEItens() {
        var resposta = rest.getForEntity("/api/clientes/1/pedidos?page=1&size=10", String.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).contains("\"codigoPedido\":1001");
        assertThat(resposta.getBody()).contains("\"valorTotal\":120.00");
        assertThat(resposta.getBody()).contains("\"quantidadeItens\":2");
        assertThat(resposta.getBody()).contains("\"totalPedidos\":1");
    }

    @Test
    @Order(4)
    void deveRetornar404ParaPedidoInexistente() {
        var resposta = rest.getForEntity("/api/pedidos/999999/total", String.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(5)
    void deveRetornar400ParaParametrosInvalidos() {
        var codigoInvalido = rest.getForEntity("/api/pedidos/0/total", String.class);
        var tipoInvalido = rest.getForEntity("/api/pedidos/abc/total", String.class);
        var paginaInvalida = rest.getForEntity("/api/clientes/1/pedidos?page=0&size=10", String.class);

        assertThat(codigoInvalido.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(tipoInvalido.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(paginaInvalida.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(6)
    void deveEncaminharMensagemInvalidaParaDlqSemTravarConsumer() {
        publicar("{ isso não é um json válido !!!");

        await().atMost(ofSeconds(30)).untilAsserted(() ->
                assertThat(rabbit.receive("pedidos.dlq", 5_000)).isNotNull());

        publicar("""
                {
                  "codigoPedido": 1002,
                  "codigoCliente": 2,
                  "itens": [
                    { "produto": "caneta", "quantidade": 3, "preco": 2.50 }
                  ]
                }
                """);

        await().atMost(ofSeconds(30)).untilAsserted(() -> {
            var resposta = rest.getForEntity("/api/pedidos/1002/total", String.class);
            assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resposta.getBody()).contains("7.50");
        });
    }

    @Test
    @Order(7)
    void healthReadyDeveEstarHealthy() {
        var resposta = rest.getForEntity("/health/ready", String.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).contains("UP");
    }

    @Test
    @Order(8)
    void deveRetornar404ParaCaminhoInexistente() {
        var resposta = rest.getForEntity("/caminho/inexistente", String.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
