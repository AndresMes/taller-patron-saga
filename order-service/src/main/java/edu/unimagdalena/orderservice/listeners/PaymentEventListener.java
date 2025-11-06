package edu.unimagdalena.orderservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unimagdalena.orderservice.config.RabbitMQConfig;
import edu.unimagdalena.orderservice.dto.PaymentCompletedEvent;
import edu.unimagdalena.orderservice.dto.PaymentFailedEvent;
import edu.unimagdalena.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_EVENT_QUEUE)
    public void handlePaymentEvent(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("Received message with routing key: {}", routingKey);

        try {
            if (RabbitMQConfig.PAYMENT_COMPLETED_ROUTING_KEY.equals(routingKey)) {
                PaymentCompletedEvent event = objectMapper.readValue(
                        message.getBody(),
                        PaymentCompletedEvent.class
                );
                handlePaymentCompletedEvent(event);
            } else if (RabbitMQConfig.PAYMENT_FAILED_ROUTING_KEY.equals(routingKey)) {
                PaymentFailedEvent event = objectMapper.readValue(
                        message.getBody(),
                        PaymentFailedEvent.class
                );
                handlePaymentFailedEvent(event);
            }
        } catch (Exception e) {
            log.error("Error processing payment event", e);
        }
    }

    private void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        log.info("Processing PaymentCompletedEvent: {}", event);
        orderService.handlePaymentCompleted(event.orderId());
    }

    private void handlePaymentFailedEvent(PaymentFailedEvent event) {
        log.info("Processing PaymentFailedEvent: {}", event);
        orderService.handlePaymentFailed(
                event.orderId(),
                event.reason()
        );
    }
}