package com.culturalspace.paymentprocessingservice.controller;

import com.culturalspace.paymentprocessingservice.model.PaymentTransaction;
import com.culturalspace.paymentprocessingservice.service.PaymentProcessingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentProcessingController {

    private final PaymentProcessingService paymentProcessingService;

    public PaymentProcessingController(PaymentProcessingService paymentProcessingService) {
        this.paymentProcessingService = paymentProcessingService;
    }

    public static class ProcessPaymentRequest {
        private Long bookingId;
        private BigDecimal amount;
        private String paymentMethod;

        public Long getBookingId() { return bookingId; }
        public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentTransaction> processPayment(@RequestBody ProcessPaymentRequest request) {
        try {
            PaymentTransaction transaction = paymentProcessingService.processPayment(
                    request.getBookingId(), request.getAmount(), request.getPaymentMethod());
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.err.println("Controller error during payment processing: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/{transactionId}/refund")
    public ResponseEntity<PaymentTransaction> refundPayment(@PathVariable String transactionId) {
        try {
            PaymentTransaction refundedTransaction = paymentProcessingService.refundPayment(transactionId);
            return ResponseEntity.ok(refundedTransaction);
        } catch (RuntimeException e) {
            System.err.println("Controller error during payment refund: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<PaymentTransaction> getPaymentById(@PathVariable String transactionId) {
        return paymentProcessingService.getPaymentById(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PaymentTransaction>> getAllPayments() {
        List<PaymentTransaction> payments = paymentProcessingService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/by-booking/{bookingId}")
    public ResponseEntity<List<PaymentTransaction>> getPaymentsByBookingId(@PathVariable Long bookingId) {
        List<PaymentTransaction> payments = paymentProcessingService.getPaymentsByBookingId(bookingId);
        return ResponseEntity.ok(payments);
    }
}