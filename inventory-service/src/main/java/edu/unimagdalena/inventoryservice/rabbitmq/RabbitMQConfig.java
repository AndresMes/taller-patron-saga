package edu.unimagdalena.inventoryservice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres de exchanges
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String INVENTORY_EVENTS_EXCHANGE = "inventory.events.exchange";

    // Cola de comandos
    public static final String RESERVE_INVENTORY_QUEUE = "reserve.inventory.queue";
    public static final String RESERVE_INVENTORY_ROUTING_KEY = "inventory.reserve";

    // Routing keys de eventos
    public static final String INVENTORY_RESERVED_ROUTING_KEY = "inventory.reserved";
    public static final String INVENTORY_REJECTED_ROUTING_KEY = "inventory.rejected";

    // Exchange para comandos entrantes
    @Bean
    public DirectExchange inventoryExchange() {
        return new DirectExchange(INVENTORY_EXCHANGE);
    }

    // Exchange para eventos salientes
    @Bean
    public TopicExchange inventoryEventsExchange() {
        return new TopicExchange(INVENTORY_EVENTS_EXCHANGE);
    }

    // Cola para recibir comandos de reserva
    @Bean
    public Queue reserveInventoryQueue() {
        return QueueBuilder.durable(RESERVE_INVENTORY_QUEUE)
                .build();
    }

    // Binding: comando de reserva
    @Bean
    public Binding reserveInventoryBinding() {
        return BindingBuilder
                .bind(reserveInventoryQueue())
                .to(inventoryExchange())
                .with(RESERVE_INVENTORY_ROUTING_KEY);
    }

    // Convertidor JSON
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate configurado
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
