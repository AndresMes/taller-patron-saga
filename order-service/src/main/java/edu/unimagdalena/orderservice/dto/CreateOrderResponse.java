package edu.unimagdalena.orderservice.dto;

import edu.unimagdalena.orderservice.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateOrderResponse(
        UUID orderId,
        String productId,
        Integer quantity,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt,
        String message
) {}