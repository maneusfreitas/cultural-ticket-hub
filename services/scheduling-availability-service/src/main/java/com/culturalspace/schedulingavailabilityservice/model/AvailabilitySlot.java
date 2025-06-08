package com.culturalspace.schedulingavailabilityservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime; // Use LocalDateTime for date and time

@Entity
@Table(name = "availability_slot") // Table name for availability slots
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eventId; // Foreign key to Event in event-catalog-service

    @Column // Optional: Foreign key to User (Operator) in user-profile-service
    private Long operatorId;

    @Column(nullable = false)
    private LocalDateTime startTime; // Start date and time of the slot

    @Column(nullable = false)
    private LocalDateTime endTime; // End date and time of the slot

    @Column(nullable = false)
    private String location; // Physical location of the event instance

    @Column(nullable = false)
    private Integer totalSeats; // Total number of seats available for this slot

    @Column(nullable = false)
    private Integer availableSeats; // Current number of available seats (decreases as tickets are booked)

    // --- Constructors ---
    public AvailabilitySlot() {}

    public AvailabilitySlot(Long eventId, Long operatorId, LocalDateTime startTime, LocalDateTime endTime, String location, Integer totalSeats, Integer availableSeats) {
        this.eventId = eventId;
        this.operatorId = operatorId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public Long getEventId() { return eventId; }
    public Long getOperatorId() { return operatorId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getLocation() { return location; }
    public Integer getTotalSeats() { return totalSeats; }
    public Integer getAvailableSeats() { return availableSeats; }

    // --- Setters ---
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setLocation(String location) { this.location = location; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    // --- toString() ---
    @Override
    public String toString() {
        return "AvailabilitySlot{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", operatorId=" + operatorId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", totalSeats=" + totalSeats +
                ", availableSeats=" + availableSeats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilitySlot that = (AvailabilitySlot) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}