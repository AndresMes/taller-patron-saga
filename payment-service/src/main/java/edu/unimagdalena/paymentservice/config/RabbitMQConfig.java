package edu.unimagdalena.paymentservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ⚠️ CAMBIOS: Estos nombres DEBEN coincidir con order-service
    public static final String PAYMENT_COMMAND_EXCHANGE = "payment.command.exchange";
    public static final String PROCESS_PAYMENT_QUEUE = "process.payment.queue";
    public static final String PROCESS_PAYMENT_ROUTING_KEY = "payment.process";

    // Exchange para eventos salientes (hacia order-service)
    public static final String PAYMENT_EVENT_EXCHANGE = "payment.event.exchange";
    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.completed";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.failed";

    @Bean
    public DirectExchange paymentCommandExchange() {
        return new DirectExchange(PAYMENT_COMMAND_EXCHANGE);
    }

    @Bean
    public TopicExchange paymentEventExchange() {
        return new TopicExchange(PAYMENT_EVENT_EXCHANGE);
    }

    @Bean
    public Queue processPaymentQueue() {
        return new Queue(PROCESS_PAYMENT_QUEUE, true);
    }

    @Bean
    public Binding processPaymentBinding() {
        return BindingBuilder
                .bind(processPaymentQueue())
                .to(paymentCommandExchange())
                .with(PROCESS_PAYMENT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}