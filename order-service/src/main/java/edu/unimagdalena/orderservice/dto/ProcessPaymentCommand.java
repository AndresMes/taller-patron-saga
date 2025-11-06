package edu.unimagdalena.orderservice.dto;

import java.math.BigDecimal;

public record ProcessPaymentCommand(
        String orderId,
        BigDecimal amount
) {}