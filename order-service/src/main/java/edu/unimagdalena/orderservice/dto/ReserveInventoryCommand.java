package edu.unimagdalena.orderservice.dto;

public record ReserveInventoryCommand(
        String orderId,
        String productId,
        Integer quantity
) {}