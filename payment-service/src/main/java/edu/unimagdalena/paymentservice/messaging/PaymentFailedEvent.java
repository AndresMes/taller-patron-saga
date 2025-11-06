package edu.unimagdalena.paymentservice.messaging;

import java.math.BigDecimal;

public record PaymentFailedEvent(
        String orderId,
        BigDecimal amount,  // ⚠️ NUEVO CAMPO REQUERIDO
        String reason
) {}