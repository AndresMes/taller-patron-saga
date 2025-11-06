package edu.unimagdalena.orderservice.service.impl;

import edu.unimagdalena.orderservice.config.RabbitMQConfig;
import edu.unimagdalena.orderservice.dto.*;
import edu.unimagdalena.orderservice.entity.Order;
import edu.unimagdalena.orderservice.enums.OrderStatus;
import edu.unimagdalena.orderservice.repository.OrderRepository;
import edu.unimagdalena.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for productId: {}, quantity: {}", request.productId(), request.quantity());

        // 1. Crear la orden con estado CREATED
        Order order = Order.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.ZERO) // Se actualizará cuando inventory responda
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order created with ID: {}", savedOrder.getId());

        // 2. Enviar comando para reservar inventario
        ReserveInventoryCommand command = new ReserveInventoryCommand(
                savedOrder.getId().toString(),
                request.productId(),
                request.quantity()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.INVENTORY_COMMAND_EXCHANGE,
                RabbitMQConfig.RESERVE_INVENTORY_ROUTING_KEY,
                command
        );

        log.info("ReserveInventoryCommand sent for orderId: {}", savedOrder.getId());

        // 3. Retornar respuesta
        return new CreateOrderResponse(
                savedOrder.getId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt(),
                "Order created successfully. Waiting for inventory confirmation."
        );
    }

    @Override
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    @Override
    @Transactional
    public void handleInventoryReserved(String orderId, String productId, Integer quantity, BigDecimal totalAmount) {
        log.info("Handling InventoryReservedEvent for orderId: {}", orderId);

        Order order = getOrderById(UUID.fromString(orderId));

        // Actualizar el monto total y cambiar estado a PENDING_PAYMENT
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderRepository.save(order);

        log.info("Order {} updated to PENDING_PAYMENT with amount: {}", orderId, totalAmount);

        // Enviar comando para procesar el pago
        ProcessPaymentCommand paymentCommand = new ProcessPaymentCommand(
                orderId,
                totalAmount
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_COMMAND_EXCHANGE,
                RabbitMQConfig.PROCESS_PAYMENT_ROUTING_KEY,
                paymentCommand
        );

        log.info("ProcessPaymentCommand sent for orderId: {}", orderId);
    }

    @Override
    @Transactional
    public void handleInventoryRejected(String orderId, String reason) {
        log.warn("Handling InventoryRejectedEvent for orderId: {}. Reason: {}", orderId, reason);

        Order order = getOrderById(UUID.fromString(orderId));
        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);

        log.info("Order {} marked as REJECTED", orderId);
    }

    @Override
    @Transactional
    public void handlePaymentCompleted(String orderId) {
        log.info("Handling PaymentCompletedEvent for orderId: {}", orderId);

        Order order = getOrderById(UUID.fromString(orderId));
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        log.info("Order {} marked as COMPLETED", orderId);

        // Aquí podrías emitir un OrderCompletedEvent si fuera necesario
    }

    @Override
    @Transactional
    public void handlePaymentFailed(String orderId, String reason) {
        log.warn("Handling PaymentFailedEvent for orderId: {}. Reason: {}", orderId, reason);

        Order order = getOrderById(UUID.fromString(orderId));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order {} marked as CANCELLED", orderId);

        // Enviar comando de compensación para liberar el inventario
        ReleaseInventoryCommand releaseCommand = new ReleaseInventoryCommand(
                orderId,
                order.getProductId(),
                order.getQuantity()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.INVENTORY_COMMAND_EXCHANGE,
                RabbitMQConfig.RELEASE_INVENTORY_ROUTING_KEY,
                releaseCommand
        );

        log.info("ReleaseInventoryCommand sent for orderId: {}", orderId);
    }
}