package com.br.pedidos.listener;

import com.br.pedidos.dto.rabbit.BaixaEstoqueEvent;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoCanceladoListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pedido.cancelar.queue", durable = "true"),
            exchange = @Exchange(value = "pedido.exchange", type = "direct", ignoreDeclarationExceptions = "true"),
            key = "pedido.cancelar.rk"
    ))
    public void receberCancelamento(BaixaEstoqueEvent evento){
        System.out.println("AVISO DO ESTOQUE RECEBIDO!");
        System.out.println("O pedido ID: " + evento.pedidoId() + " falhou por falta de estoque.");
        System.out.println("Status do pedido alterado para CANCELADO com sucesso!");
    }
}
