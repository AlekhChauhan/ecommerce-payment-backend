package com.ecommerce.payment.dto;

public class PaymentRequest {
    private Long amount; // Amount in cents (e.g., 1000 = $10.00)
    private String currency;

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

}
