package edu.unimagdalena.orderservice.dto;

import java.math.BigDecimal;

public record InventoryReservedEvent(
        String orderId,
        String productId,
        Integer quantity,
        BigDecimal totalAmount
) {}