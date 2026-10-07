package com.br.pedidos.listener;

import com.br.pedidos.dto.rabbit.PedidoAprovadoEvent;
import com.br.pedidos.services.PedidoService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PedidoAprovadoListener {

    private final PedidoService pedidoService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pedido.aprovar.queue", durable = "true"),
            exchange = @Exchange(value = "pedido.exchange", durable = "true", ignoreDeclarationExceptions = "true"),
            key = "pedido.aprovar.rk"
    ))
    public void aprovarPedido(PedidoAprovadoEvent evento){
        pedidoService.aprovarPedido(evento.pedidoId());
    }
}
