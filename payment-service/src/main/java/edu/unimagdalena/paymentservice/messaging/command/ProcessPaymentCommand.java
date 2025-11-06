package edu.unimagdalena.paymentservice.messaging.command;

import java.math.BigDecimal;

public record ProcessPaymentCommand(
        String orderId,
        BigDecimal amount
) {}
