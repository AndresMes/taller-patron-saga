package edu.unimagdalena.inventoryservice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String INVENTORY_COMMAND_EXCHANGE = "inventory.command.exchange";
    public static final String RESERVE_INVENTORY_QUEUE = "reserve.inventory.queue";
    public static final String RESERVE_INVENTORY_ROUTING_KEY = "inventory.reserve";
    public static final String RELEASE_INVENTORY_ROUTING_KEY = "inventory.release";

    public static final String INVENTORY_EVENT_EXCHANGE = "inventory.event.exchange";
    public static final String INVENTORY_RESERVED_ROUTING_KEY = "inventory.reserved";
    public static final String INVENTORY_REJECTED_ROUTING_KEY = "inventory.rejected";

    @Bean
    public DirectExchange inventoryCommandExchange() {
        return new DirectExchange(INVENTORY_COMMAND_EXCHANGE);
    }

    @Bean
    public TopicExchange inventoryEventExchange() {
        return new TopicExchange(INVENTORY_EVENT_EXCHANGE);
    }

    @Bean
    public Queue reserveInventoryQueue() {
        return QueueBuilder.durable(RESERVE_INVENTORY_QUEUE).build();
    }

    @Bean
    public Binding reserveInventoryBinding() {
        return BindingBuilder
                .bind(reserveInventoryQueue())
                .to(inventoryCommandExchange())
                .with(RESERVE_INVENTORY_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(new SimpleNameClassMapper()); // 👈 nuestro mapper personalizado
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}