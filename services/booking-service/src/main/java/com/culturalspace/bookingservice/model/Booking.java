package com.culturalspace.bookingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long availabilitySlotId; // The slot for which seats are being booked

    @Column(nullable = false)
    private Integer numberOfSeats;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @Column(nullable = false)
    private String status; // PENDING, CONFIRMED, FAILED, CANCELLED

    private Long reservationId; // ID from seat-reservation-service
    private String paymentTransactionId; // ID from payment-processing-service
    private String promoCode;

    public Booking() {}

    // Updated constructor to include promoCode
    public Booking(Long userId, Long availabilitySlotId, Integer numberOfSeats, BigDecimal totalAmount,
                   LocalDateTime bookingTime, String status, String promoCode) { // promoCode added here
        this.userId = userId;
        this.availabilitySlotId = availabilitySlotId;
        this.numberOfSeats = numberOfSeats;
        this.totalAmount = totalAmount;
        this.bookingTime = bookingTime;
        this.status = status;
        this.promoCode = promoCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getAvailabilitySlotId() { return availabilitySlotId; }
    public void setAvailabilitySlotId(Long availabilitySlotId) { this.availabilitySlotId = availabilitySlotId; }
    public Integer getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(Integer numberOfSeats) { this.numberOfSeats = numberOfSeats; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    public String getPaymentTransactionId() { return paymentTransactionId; }
    public void setPaymentTransactionId(String paymentTransactionId) { this.paymentTransactionId = paymentTransactionId; }
    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userId=" + userId +
                ", availabilitySlotId=" + availabilitySlotId +
                ", numberOfSeats=" + numberOfSeats +
                ", totalAmount=" + totalAmount +
                ", bookingTime=" + bookingTime +
                ", status='" + status + '\'' +
                ", reservationId=" + reservationId +
                ", paymentTransactionId='" + paymentTransactionId + '\'' +
                ", promoCode='" + promoCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id != null && id.equals(booking.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}