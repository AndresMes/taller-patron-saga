package edu.unimagdalena.paymentservice.messaging;

import edu.unimagdalena.paymentservice.service.PaymentProcessorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentMessageListener {

    private final PaymentProcessorService paymentService;

    public PaymentMessageListener(PaymentProcessorService paymentService) {
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = "process.payment.queue")
    public void handleProcessPayment(ProcessPaymentCommand command) {
        System.out.println("Comando recibido en payment-service: " + command);
        paymentService.processPayment(command);
    }
}
