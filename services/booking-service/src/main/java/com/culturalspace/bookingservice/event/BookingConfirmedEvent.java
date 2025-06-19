package com.culturalspace.bookingservice.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingConfirmedEvent {
    private Long bookingId;
    private Long userId;
    private int numberOfSeats;
    private double totalPrice;

    // Default constructor is required for JSON deserialization by Kafka/Spring
    public BookingConfirmedEvent() {}

    public BookingConfirmedEvent(Long bookingId, Long userId, int numberOfSeats, double totalPrice) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.numberOfSeats = numberOfSeats;
        this.totalPrice = totalPrice;
    }

    // --- Getters ---
    public Long getBookingId() { return bookingId; }
    public Long getUserId() { return userId; }
    public int getNumberOfSeats() { return numberOfSeats; }
    public double getTotalPrice() { return totalPrice; }

    // --- Setters ---
    // (Setters are often needed for deserialization, though not always used if data is immutable)
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return "BookingConfirmedEvent{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", numberOfSeats=" + numberOfSeats +
                ", totalPrice=" + totalPrice +
                '}';
    }
}