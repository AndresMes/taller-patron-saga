package edu.unimagdalena.orderservice.dto;

public record ReleaseInventoryCommand(
        String orderId,
        String productId,
        Integer quantity
) {}