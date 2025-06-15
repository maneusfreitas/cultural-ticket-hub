package com.culturalspace.notificationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;
    private Long userId;
    private LocalDateTime logTimestamp;

    // Default constructor for JPA
    public NotificationLog() {}

    // Constructor to easily create log entries from Kafka event
    public NotificationLog(Long bookingId, Long userId, LocalDateTime logTimestamp) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.logTimestamp = logTimestamp;
    }

    public Long getId() {
        return id;
    }
}