package com.culturalspace.paymentprocessingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transaction")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Using UUID for transaction IDs
    private String transactionId;

    @Column(nullable = false)
    private Long bookingId; // Reference to the booking that initiated this payment

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentMethod; // e.g., "CARD", "PAYPAL", "BANK_TRANSFER"

    @Column(nullable = false)
    private LocalDateTime transactionTime;

    @Column(nullable = false)
    private String status; // e.g., "PENDING", "SUCCESS", "FAILED", "REFUNDED"

    // Optional: Gateway response code, message, etc.
    private String gatewayResponseCode;
    private String gatewayResponseMessage;

    public PaymentTransaction() {}

    public PaymentTransaction(Long bookingId, BigDecimal amount, String paymentMethod, LocalDateTime transactionTime, String status) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionTime = transactionTime;
        this.status = status;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getTransactionTime() { return transactionTime; }
    public void setTransactionTime(LocalDateTime transactionTime) { this.transactionTime = transactionTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getGatewayResponseCode() { return gatewayResponseCode; }
    public void setGatewayResponseCode(String gatewayResponseCode) { this.gatewayResponseCode = gatewayResponseCode; }
    public String getGatewayResponseMessage() { return gatewayResponseMessage; }
    public void setGatewayResponseMessage(String gatewayResponseMessage) { this.gatewayResponseMessage = gatewayResponseMessage; }

    @Override
    public String toString() {
        return "PaymentTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", bookingId=" + bookingId +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", transactionTime=" + transactionTime +
                ", status='" + status + '\'' +
                ", gatewayResponseCode='" + gatewayResponseCode + '\'' +
                ", gatewayResponseMessage='" + gatewayResponseMessage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentTransaction that = (PaymentTransaction) o;
        return transactionId != null && transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return transactionId != null ? transactionId.hashCode() : 0;
    }
}