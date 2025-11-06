package edu.unimagdalena.orderservice.dto;

import java.math.BigDecimal;

public record PaymentCompletedEvent(
        String orderId,
        BigDecimal amount
) {}