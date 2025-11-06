package edu.unimagdalena.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange para comandos de inventario
    public static final String INVENTORY_COMMAND_EXCHANGE = "inventory.command.exchange";
    public static final String RESERVE_INVENTORY_ROUTING_KEY = "inventory.reserve";
    public static final String RELEASE_INVENTORY_ROUTING_KEY = "inventory.release";

    // Exchange para comandos de pago
    public static final String PAYMENT_COMMAND_EXCHANGE = "payment.command.exchange";
    public static final String PROCESS_PAYMENT_ROUTING_KEY = "payment.process";

    // Cola para eventos de inventario
    public static final String INVENTORY_EVENT_QUEUE = "order.inventory.event.queue";
    public static final String INVENTORY_EVENT_EXCHANGE = "inventory.event.exchange";
    public static final String INVENTORY_RESERVED_ROUTING_KEY = "inventory.reserved";
    public static final String INVENTORY_REJECTED_ROUTING_KEY = "inventory.rejected";

    // Cola para eventos de pago
    public static final String PAYMENT_EVENT_QUEUE = "order.payment.event.queue";
    public static final String PAYMENT_EVENT_EXCHANGE = "payment.event.exchange";
    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.completed";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.failed";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange inventoryCommandExchange() {
        return new DirectExchange(INVENTORY_COMMAND_EXCHANGE);
    }

    @Bean
    public DirectExchange paymentCommandExchange() {
        return new DirectExchange(PAYMENT_COMMAND_EXCHANGE);
    }
    
    @Bean
    public Queue inventoryEventQueue() {
        return new Queue(INVENTORY_EVENT_QUEUE, true);
    }

    @Bean
    public TopicExchange inventoryEventExchange() {
        return new TopicExchange(INVENTORY_EVENT_EXCHANGE);
    }

    @Bean
    public Binding inventoryReservedBinding() {
        return BindingBuilder
                .bind(inventoryEventQueue())
                .to(inventoryEventExchange())
                .with(INVENTORY_RESERVED_ROUTING_KEY);
    }

    @Bean
    public Binding inventoryRejectedBinding() {
        return BindingBuilder
                .bind(inventoryEventQueue())
                .to(inventoryEventExchange())
                .with(INVENTORY_REJECTED_ROUTING_KEY);
    }

    // Configuración para eventos de Pago
    @Bean
    public Queue paymentEventQueue() {
        return new Queue(PAYMENT_EVENT_QUEUE, true);
    }

    @Bean
    public TopicExchange paymentEventExchange() {
        return new TopicExchange(PAYMENT_EVENT_EXCHANGE);
    }

    @Bean
    public Binding paymentCompletedBinding() {
        return BindingBuilder
                .bind(paymentEventQueue())
                .to(paymentEventExchange())
                .with(PAYMENT_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder
                .bind(paymentEventQueue())
                .to(paymentEventExchange())
                .with(PAYMENT_FAILED_ROUTING_KEY);
    }
}