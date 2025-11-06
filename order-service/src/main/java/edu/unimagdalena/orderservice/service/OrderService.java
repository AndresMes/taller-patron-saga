package edu.unimagdalena.orderservice.service;

import edu.unimagdalena.orderservice.dto.CreateOrderRequest;
import edu.unimagdalena.orderservice.dto.CreateOrderResponse;
import edu.unimagdalena.orderservice.entity.Order;

import java.util.UUID;

public interface OrderService {

    // Crear una nueva orden
    CreateOrderResponse createOrder(CreateOrderRequest request);

    // Obtener una orden por ID
    Order getOrderById(UUID orderId);

    // Métodos para actualizar el estado de la orden durante la saga
    void handleInventoryReserved(String orderId, String productId, Integer quantity, java.math.BigDecimal totalAmount);

    void handleInventoryRejected(String orderId, String reason);

    void handlePaymentCompleted(String orderId);

    void handlePaymentFailed(String orderId, String reason);
}