package edu.unimagdalena.orderservice.dto;

import java.math.BigDecimal;

public record PaymentFailedEvent(
        String orderId,
        BigDecimal amount,
        String reason
) {}