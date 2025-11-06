package edu.unimagdalena.inventoryservice.rabbitmq;

import edu.unimagdalena.inventoryservice.DTO.InventoryRejectedEvent;
import edu.unimagdalena.inventoryservice.DTO.InventoryReservedEvent;
import edu.unimagdalena.inventoryservice.DTO.ReserveInventoryCommand;
import edu.unimagdalena.inventoryservice.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryCommandListener {

    private final InventoryItemService inventoryItemService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.RESERVE_INVENTORY_QUEUE)
    public void handleReserveInventory(ReserveInventoryCommand command) {
        log.info("Recibido comando de reserva: OrderId={}, ProductId={}, Quantity={}",
                command.getOrderId(), command.getProductId(), command.getQuantity());

        try {
            boolean reserved = inventoryItemService.reserveInventory(command);

            if (reserved) {
                // ✅ Stock suficiente → Emitir evento de éxito
                InventoryReservedEvent event = new InventoryReservedEvent(
                        command.getOrderId(),
                        command.getProductId(),
                        command.getQuantity(),
                        "Inventario reservado exitosamente"
                );

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.INVENTORY_EVENTS_EXCHANGE,
                        RabbitMQConfig.INVENTORY_RESERVED_ROUTING_KEY,
                        event
                );

                log.info("✅ Inventario reservado para orden: {}", command.getOrderId());

            } else {
                // ❌ Stock insuficiente → Emitir evento de rechazo
                Long availableQty = inventoryItemService.getAvailableQuantity(command.getProductId());

                InventoryRejectedEvent event = new InventoryRejectedEvent(
                        command.getOrderId(),
                        command.getProductId(),
                        command.getQuantity(),
                        availableQty,
                        "Stock insuficiente. Disponible: " + availableQty + ", Solicitado: " + command.getQuantity()
                );

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.INVENTORY_EVENTS_EXCHANGE,
                        RabbitMQConfig.INVENTORY_REJECTED_ROUTING_KEY,
                        event
                );

                log.warn("❌ Reserva rechazada para orden: {} - Stock insuficiente", command.getOrderId());
            }

        } catch (Exception e) {
            log.error("Error procesando reserva de inventario: {}", e.getMessage(), e);

            // En caso de error, también emitir rechazo
            InventoryRejectedEvent errorEvent = new InventoryRejectedEvent(
                    command.getOrderId(),
                    command.getProductId(),
                    command.getQuantity(),
                    0L,
                    "Error al procesar reserva: " + e.getMessage()
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.INVENTORY_EVENTS_EXCHANGE,
                    RabbitMQConfig.INVENTORY_REJECTED_ROUTING_KEY,
                    errorEvent
            );
        }
    }
}
