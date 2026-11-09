package com.btg.orders.api.web;

import com.btg.orders.application.caso.ConsultarPedidosClienteUseCase;
import com.btg.orders.application.dto.ContagemPedidosClienteResponse;
import com.btg.orders.application.dto.PaginaPedidosResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ClientePedidoController {

    private final ConsultarPedidosClienteUseCase consultarPedidosCliente;

    public ClientePedidoController(ConsultarPedidosClienteUseCase consultarPedidosCliente) {
        this.consultarPedidosCliente = consultarPedidosCliente;
    }

    @GetMapping("/clientes/{codigoCliente}/pedidos/quantidade")
    public ContagemPedidosClienteResponse contar(@PathVariable int codigoCliente) {
        return consultarPedidosCliente.contar(codigoCliente);
    }

    @GetMapping("/clientes/{codigoCliente}/pedidos")
    public PaginaPedidosResponse listar(@PathVariable int codigoCliente,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return consultarPedidosCliente.listar(codigoCliente, page, size);
    }
}
