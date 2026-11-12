package com.ecommerce.payment.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private String currency;
    private String status;
    private String paymentIntentId;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt=LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id=id; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount=amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency=currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status=status; }
    public String getPaymentIntentId(){return paymentIntentId;}
    public void setPaymentIntentId(String paymentIntentId){this.paymentIntentId=paymentIntentId;}
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt=createdAt; }
}