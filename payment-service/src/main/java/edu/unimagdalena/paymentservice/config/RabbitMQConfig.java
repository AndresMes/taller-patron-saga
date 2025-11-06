package edu.unimagdalena.paymentservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String PROCESS_PAYMENT_QUEUE = "process.payment.queue";
    public static final String ROUTING_PROCESS_PAYMENT = "payment.process";
    public static final String ROUTING_PAYMENT_COMPLETED = "payment.completed";
    public static final String ROUTING_PAYMENT_FAILED = "payment.failed";

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue processPaymentQueue() {
        return new Queue(PROCESS_PAYMENT_QUEUE, true); 
    }

    @Bean
    public Binding processPaymentBinding() {
        return BindingBuilder
                .bind(processPaymentQueue())
                .to(paymentExchange())
                .with(ROUTING_PROCESS_PAYMENT);
    }
}
