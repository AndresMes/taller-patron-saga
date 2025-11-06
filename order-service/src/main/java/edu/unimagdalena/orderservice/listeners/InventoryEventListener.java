package edu.unimagdalena.orderservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unimagdalena.orderservice.config.RabbitMQConfig;
import edu.unimagdalena.orderservice.dto.InventoryRejectedEvent;
import edu.unimagdalena.orderservice.dto.InventoryReservedEvent;
import edu.unimagdalena.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventListener {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_EVENT_QUEUE)
    public void handleInventoryEvent(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("Received message with routing key: {}", routingKey);

        try {
            if (RabbitMQConfig.INVENTORY_RESERVED_ROUTING_KEY.equals(routingKey)) {
                InventoryReservedEvent event = objectMapper.readValue(
                        message.getBody(),
                        InventoryReservedEvent.class
                );
                handleInventoryReservedEvent(event);
            } else if (RabbitMQConfig.INVENTORY_REJECTED_ROUTING_KEY.equals(routingKey)) {
                InventoryRejectedEvent event = objectMapper.readValue(
                        message.getBody(),
                        InventoryRejectedEvent.class
                );
                handleInventoryRejectedEvent(event);
            }
        } catch (Exception e) {
            log.error("Error processing inventory event", e);
        }
    }

    private void handleInventoryReservedEvent(InventoryReservedEvent event) {
        log.info("Processing InventoryReservedEvent: {}", event);
        orderService.handleInventoryReserved(
                event.orderId(),
                event.productId(),
                event.quantity(),
                event.totalAmount()
        );
    }

    private void handleInventoryRejectedEvent(InventoryRejectedEvent event) {
        log.info("Processing InventoryRejectedEvent: {}", event);
        orderService.handleInventoryRejected(
                event.orderId(),
                event.reason()
        );
    }
}