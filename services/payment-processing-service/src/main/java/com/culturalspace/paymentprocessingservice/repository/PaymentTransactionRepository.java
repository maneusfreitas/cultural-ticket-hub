package com.culturalspace.paymentprocessingservice.repository;

import com.culturalspace.paymentprocessingservice.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, String> {
    List<PaymentTransaction> findByBookingId(Long bookingId);
    List<PaymentTransaction> findByStatus(String status);
}