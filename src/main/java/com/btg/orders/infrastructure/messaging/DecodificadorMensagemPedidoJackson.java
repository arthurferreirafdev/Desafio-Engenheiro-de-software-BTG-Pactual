package com.btg.orders.infrastructure.messaging;

import com.btg.orders.application.MensagemInvalidaException;
import com.btg.orders.application.dto.PedidoMessage;
import com.btg.orders.application.porta.DecodificadorMensagemPedido;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.stereotype.Component;

/** Decodifica o corpo da mensagem (JSON) para o DTO de aplicação. */
@Component
public class DecodificadorMensagemPedidoJackson implements DecodificadorMensagemPedido {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public PedidoMessage decodificar(byte[] corpo) {
        try {
            return MAPPER.readValue(corpo, PedidoMessage.class);
        } catch (IOException e) {
            throw new MensagemInvalidaException("JSON malformado: " + e.getMessage(), e);
        }
    }
}
