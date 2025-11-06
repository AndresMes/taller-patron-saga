package edu.unimagdalena.paymentservice.service;

import edu.unimagdalena.paymentservice.messaging.command.ProcessPaymentCommand;
import edu.unimagdalena.paymentservice.messaging.event.PaymentCompletedEvent;
import edu.unimagdalena.paymentservice.messaging.event.PaymentFailedEvent;
import edu.unimagdalena.paymentservice.model.Payment;
import edu.unimagdalena.paymentservice.model.PaymentStatus;
import edu.unimagdalena.paymentservice.repository.PaymentRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static edu.unimagdalena.paymentservice.config.RabbitMQConfig.*;

@Service
public class PaymentProcessorService {

    private final RabbitTemplate rabbitTemplate;
    private final PaymentRepository paymentRepository;

    public PaymentProcessorService(RabbitTemplate rabbitTemplate, PaymentRepository paymentRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.paymentRepository = paymentRepository;
    }

    public void processPayment(ProcessPaymentCommand cmd) {

        UUID orderId = UUID.fromString(cmd.orderId());

        boolean approved = cmd.amount().doubleValue() <= 100;

        if (approved) {
            paymentRepository.save(new Payment(
                    orderId,
                    cmd.amount(),
                    PaymentStatus.SUCCESS
            ));

            rabbitTemplate.convertAndSend(
                    PAYMENT_EXCHANGE,
                    ROUTING_PAYMENT_COMPLETED,
                    new PaymentCompletedEvent(cmd.orderId())
            );

            System.out.println(" Pago exitoso para orden " + cmd.orderId());

        } else {
            paymentRepository.save(new Payment(
                    orderId,
                    cmd.amount(),
                    PaymentStatus.FAILED
            ));

            rabbitTemplate.convertAndSend(
                    PAYMENT_EXCHANGE,
                    ROUTING_PAYMENT_FAILED,
                    new PaymentFailedEvent(cmd.orderId(), "Pago rechazado: monto excedido")
            );

            System.out.println("Pago fallido para orden " + cmd.orderId());
        }
    }
}
