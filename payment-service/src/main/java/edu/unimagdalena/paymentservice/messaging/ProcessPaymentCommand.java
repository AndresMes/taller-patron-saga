package edu.unimagdalena.paymentservice.messaging;

import java.math.BigDecimal;

public record ProcessPaymentCommand(
        String orderId,
        BigDecimal amount
) {}
