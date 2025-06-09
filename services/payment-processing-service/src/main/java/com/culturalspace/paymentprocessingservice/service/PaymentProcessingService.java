package com.culturalspace.paymentprocessingservice.service;

import com.culturalspace.paymentprocessingservice.model.PaymentTransaction;
import com.culturalspace.paymentprocessingservice.repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentProcessingService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentProcessingService(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @Transactional
    public PaymentTransaction processPayment(Long bookingId, BigDecimal amount, String paymentMethod) {
        System.out.println("PaymentProcessingService: Attempting to process payment for Booking ID: " + bookingId + ", Amount: " + amount);

        PaymentTransaction transaction = new PaymentTransaction(bookingId, amount, paymentMethod, LocalDateTime.now(), "PENDING");
        // Simulate payment gateway interaction
        // For demonstration, let's always succeed unless amount is negative or 0
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            transaction.setStatus("FAILED");
            transaction.setGatewayResponseCode("400");
            transaction.setGatewayResponseMessage("Invalid Amount");
            System.err.println("PaymentProcessingService: Payment FAILED for Booking ID: " + bookingId + " due to invalid amount.");
        } else if (amount.compareTo(new BigDecimal("1000")) > 0) {
            // Simulate a random failure for large amounts for testing failure scenarios
            if (Math.random() < 0.5) { // 50% chance of failure for amounts > 1000
                transaction.setStatus("FAILED");
                transaction.setGatewayResponseCode("500");
                transaction.setGatewayResponseMessage("Gateway error (simulated)");
                System.err.println("PaymentProcessingService: Payment FAILED for Booking ID: " + bookingId + " (simulated gateway error for large amount).");
            } else {
                transaction.setStatus("SUCCESS");
                transaction.setGatewayResponseCode("200");
                transaction.setGatewayResponseMessage("Payment successful (simulated)");
                System.out.println("PaymentProcessingService: Payment SUCCESS for Booking ID: " + bookingId + ".");
            }
        }
        else {
            transaction.setStatus("SUCCESS");
            transaction.setGatewayResponseCode("200");
            transaction.setGatewayResponseMessage("Payment successful (simulated)");
            System.out.println("PaymentProcessingService: Payment SUCCESS for Booking ID: " + bookingId + ".");
        }

        return paymentTransactionRepository.save(transaction);
    }

    @Transactional
    public PaymentTransaction refundPayment(String transactionId) {
        System.out.println("PaymentProcessingService: Attempting to refund transaction ID: " + transactionId);
        return paymentTransactionRepository.findById(transactionId)
                .map(transaction -> {
                    if ("SUCCESS".equals(transaction.getStatus())) {
                        // Simulate refund process
                        transaction.setStatus("REFUNDED");
                        transaction.setGatewayResponseCode("200"); // Assuming success for refund
                        transaction.setGatewayResponseMessage("Refund successful (simulated)");
                        System.out.println("PaymentProcessingService: Transaction " + transactionId + " REFUNDED successfully.");
                        return paymentTransactionRepository.save(transaction);
                    } else {
                        System.err.println("PaymentProcessingService: Cannot refund transaction " + transactionId + " with status: " + transaction.getStatus());
                        throw new RuntimeException("Cannot refund transaction with current status: " + transaction.getStatus());
                    }
                })
                .orElseThrow(() -> {
                    System.err.println("PaymentProcessingService: Payment transaction not found with ID: " + transactionId);
                    return new RuntimeException("Payment transaction not found with ID: " + transactionId);
                });
    }

    @Transactional(readOnly = true)
    public Optional<PaymentTransaction> getPaymentById(String transactionId) {
        System.out.println("PaymentProcessingService: Fetching payment with ID: " + transactionId);
        return paymentTransactionRepository.findById(transactionId);
    }

    @Transactional(readOnly = true)
    public List<PaymentTransaction> getAllPayments() {
        System.out.println("PaymentProcessingService: Fetching all payments.");
        return paymentTransactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PaymentTransaction> getPaymentsByBookingId(Long bookingId) {
        System.out.println("PaymentProcessingService: Fetching payments for booking ID: " + bookingId);
        return paymentTransactionRepository.findByBookingId(bookingId);
    }
}