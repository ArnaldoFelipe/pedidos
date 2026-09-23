package com.br.pedidos.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "pedido.exchange";
    public static final String QUEUE_BAIXA_ESTOQUE = "estoque.baixar.queue";
    public static final String ROUTING_KEY_BAIXA = "estoque.baixar.rk";

    @Bean
    public Queue filaBaixaEstoque(){
        return new Queue(QUEUE_BAIXA_ESTOQUE, true);
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding bindingBaixaEstoque(Queue filaBaixaEstoque, DirectExchange exchange){
        return BindingBuilder.bind(filaBaixaEstoque).to(exchange).with(ROUTING_KEY_BAIXA);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
