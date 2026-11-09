package com.btg.orders.infrastructure.messaging;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Topologia do broker e política do consumer:
 * - Exchange direct pedidos.exchange → fila pedidos.queue (routing key pedido.criado).
 * - DLX pedidos.dlx → DLQ pedidos.dlq para mensagens inválidas ou que excederam as retentativas.
 * - Ack manual; retry in-memory (3 tentativas, backoff exponencial 1s→2s→4s);
 *   ao esgotar, a mensagem é rejeitada sem requeue e cai na DLQ.
 */
@Configuration
public class RabbitMensageriaConfig {

    public static final String FILA_PEDIDOS = "pedidos.queue";
    public static final String EXCHANGE_PEDIDOS = "pedidos.exchange";
    public static final String ROUTING_KEY_PEDIDO_CRIADO = "pedido.criado";
    public static final String EXCHANGE_DLX = "pedidos.dlx";
    public static final String FILA_DLQ = "pedidos.dlq";

    @Bean
    public Queue pedidosQueue() {
        return QueueBuilder.durable(FILA_PEDIDOS)
                .deadLetterExchange(EXCHANGE_DLX)
                .build();
    }

    @Bean
    public DirectExchange pedidosExchange() {
        return new DirectExchange(EXCHANGE_PEDIDOS);
    }

    @Bean
    public Binding pedidosBinding() {
        return BindingBuilder.bind(pedidosQueue()).to(pedidosExchange()).with(ROUTING_KEY_PEDIDO_CRIADO);
    }

    @Bean
    public DirectExchange pedidosDlx() {
        return new DirectExchange(EXCHANGE_DLX);
    }

    @Bean
    public Queue pedidosDlq() {
        return QueueBuilder.durable(FILA_DLQ).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(pedidosDlq()).to(pedidosDlx()).with(ROUTING_KEY_PEDIDO_CRIADO);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(10);
        factory.setDefaultRequeueRejected(false);

        var politicaRetry = new SimpleRetryPolicy();
        politicaRetry.setMaxAttempts(3);

        var backoff = new ExponentialBackOffPolicy();
        backoff.setInitialInterval(1_000);
        backoff.setMultiplier(2.0);
        backoff.setMaxInterval(10_000);

        var retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(politicaRetry);
        retryTemplate.setBackOffPolicy(backoff);

        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .retryOperations(retryTemplate)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build());
        return factory;
    }
}
