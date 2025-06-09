package com.culturalspace.seatreservationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long availabilitySlotId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer numberOfSeats;

    @Column(nullable = false)
    private LocalDateTime reservationTime;

    @Column(nullable = false)
    private String status; // e.g., "PENDING_REDIS_HOLD", "CONFIRMED", "CANCELLED", "FAILED"

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    public Reservation() {}

    public Reservation(Long availabilitySlotId, Long userId, Integer numberOfSeats, LocalDateTime reservationTime, String status, BigDecimal totalPrice) {
        this.availabilitySlotId = availabilitySlotId;
        this.userId = userId;
        this.numberOfSeats = numberOfSeats;
        this.reservationTime = reservationTime;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public Long getId() { return id; }
    public Long getAvailabilitySlotId() { return availabilitySlotId; }
    public Long getUserId() { return userId; }
    public Integer getNumberOfSeats() { return numberOfSeats; }
    public LocalDateTime getReservationTime() { return reservationTime; }
    public String getStatus() { return status; }
    public BigDecimal getTotalPrice() { return totalPrice; }

    public void setId(Long id) { this.id = id; }
    public void setAvailabilitySlotId(Long availabilitySlotId) { this.availabilitySlotId = availabilitySlotId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setNumberOfSeats(Integer numberOfSeats) { this.numberOfSeats = numberOfSeats; }
    public void setReservationTime(LocalDateTime reservationTime) { this.reservationTime = reservationTime; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", availabilitySlotId=" + availabilitySlotId +
                ", userId=" + userId +
                ", numberOfSeats=" + numberOfSeats +
                ", reservationTime=" + reservationTime +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}