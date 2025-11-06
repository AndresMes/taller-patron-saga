package edu.unimagdalena.paymentservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime processedAt;

    public Payment() {
        this.id = UUID.randomUUID();
        this.processedAt = LocalDateTime.now();
    }

    public Payment(UUID orderId, BigDecimal amount, PaymentStatus status) {
        this();
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
    }

    // getters y setters (o usa Lombok si quieres)
    public UUID getId() {
        return id;
    }
    public UUID getOrderId() {
        return orderId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public PaymentStatus getStatus() {
        return status;
    }
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    
}
