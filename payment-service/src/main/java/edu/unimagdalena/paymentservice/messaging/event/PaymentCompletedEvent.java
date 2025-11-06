package edu.unimagdalena.paymentservice.messaging.event;

public record PaymentCompletedEvent(
        String orderId
) {}
