package com.br.pedidos.listener;

import com.br.pedidos.dto.rabbit.BaixaEstoqueEvent;
import com.br.pedidos.services.PedidoService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PedidoCanceladoListener {

    private final PedidoService pedidoService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pedido.cancelar.queue", durable = "true"),
            exchange = @Exchange(value = "pedido.exchange", type = "direct", ignoreDeclarationExceptions = "true"),
            key = "pedido.cancelar.rk"
    ))
    public void receberCancelamento(BaixaEstoqueEvent evento){
        System.out.println("AVISO DO ESTOQUE RECEBIDO!");
        System.out.println("O pedido ID: " + evento.pedidoId() + " falhou por falta de estoque.");

        pedidoService.cancelarPedido(evento.pedidoId());

        System.out.println("Status do pedido alterado para CANCELADO com sucesso!");
    }
}
