package com.btg.orders.infrastructure.messaging;

import com.btg.orders.application.MensagemInvalidaException;
import com.btg.orders.application.caso.RegistrarPedidoUseCase;
import com.btg.orders.application.dto.PedidoMessage;
import com.btg.orders.application.dto.ResultadoRegistro;
import com.btg.orders.application.porta.DecodificadorMensagemPedido;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

/**
 * Consumer da fila de pedidos — ack manual somente após o commit;
 * mensagens inválidas e duplicadas por corrida de constraint são
 * confirmadas ou encaminhadas à DLQ sem travar o consumo.
 */
@Component
public class PedidoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoConsumer.class);

    private final DecodificadorMensagemPedido decodificador;
    private final RegistrarPedidoUseCase registrarPedido;

    public PedidoConsumer(DecodificadorMensagemPedido decodificador, RegistrarPedidoUseCase registrarPedido) {
        this.decodificador = decodificador;
        this.registrarPedido = registrarPedido;
    }

    @RabbitListener(queues = RabbitMensageriaConfig.FILA_PEDIDOS)
    public void consumir(Message mensagem, Channel canal) throws IOException {
        long deliveryTag = mensagem.getMessageProperties().getDeliveryTag();

        try {
            PedidoMessage pedido = decodificador.decodificar(mensagem.getBody());
            ResultadoRegistro resultado = registrarPedido.executar(pedido);
            log.info("Pedido codigoPedido={} codigoCliente={} resultado={} deliveryTag={}",
                    pedido.codigoPedido(), pedido.codigoCliente(), resultado, deliveryTag);
            canal.basicAck(deliveryTag, false);
        } catch (MensagemInvalidaException e) {
            log.warn("Mensagem inválida (deliveryTag={}) encaminhada à DLQ: {}", deliveryTag, e.getMessage());
            canal.basicNack(deliveryTag, false, false);
        } catch (DataIntegrityViolationException e) {
            log.warn("Pedido duplicado por constraint (deliveryTag={}) confirmado como processado: {}",
                    deliveryTag, e.getMostSpecificCause().getMessage());
            canal.basicAck(deliveryTag, false);
        }
    }
}
