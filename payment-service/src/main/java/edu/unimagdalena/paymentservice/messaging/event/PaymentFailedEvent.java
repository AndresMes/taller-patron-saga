package edu.unimagdalena.paymentservice.messaging.event;

public record PaymentFailedEvent(
        String orderId,
        String reason
) {}
