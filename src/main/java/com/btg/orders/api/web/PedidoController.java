package com.btg.orders.api.web;

import com.btg.orders.application.caso.ConsultarValorTotalPedidoUseCase;
import com.btg.orders.application.dto.TotalPedidoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PedidoController {

    private final ConsultarValorTotalPedidoUseCase consultarValorTotal;

    public PedidoController(ConsultarValorTotalPedidoUseCase consultarValorTotal) {
        this.consultarValorTotal = consultarValorTotal;
    }

    @GetMapping("/pedidos/{codigoPedido}/total")
    public TotalPedidoResponse consultarTotal(@PathVariable int codigoPedido) {
        return consultarValorTotal.executar(codigoPedido);
    }
}
