package com.btg.orders.application.porta;

import com.btg.orders.application.MensagemInvalidaException;
import com.btg.orders.application.dto.PedidoMessage;

/** Port de decodificação da mensagem bruta — implementado com Jackson na infraestrutura. */
public interface DecodificadorMensagemPedido {

    PedidoMessage decodificar(byte[] corpo) throws MensagemInvalidaException;
}
