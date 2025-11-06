package edu.unimagdalena.paymentservice.messaging;

import java.math.BigDecimal;

public record PaymentCompletedEvent(
        String orderId,
        BigDecimal amount
) {}