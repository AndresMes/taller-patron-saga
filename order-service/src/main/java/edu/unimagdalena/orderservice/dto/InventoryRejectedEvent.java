package edu.unimagdalena.orderservice.dto;

public record InventoryRejectedEvent(
        String orderId,
        String productId,
        Integer quantity,
        String reason
) {}