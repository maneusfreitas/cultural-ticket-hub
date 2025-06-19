package com.culturalspace.promotionservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // e.g., "SUMMER20", "FIRSTORDER"

    private Long eventId; // Can be null if the promotion is not event-specific

    @Column(nullable = false)
    private String type; // e.g., "PERCENTAGE", "FIXED_AMOUNT"

    @Column(nullable = false)
    private Double discountValue; // e.g., 0.20 for 20%, or 10.00 for $10 off

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private Integer usageLimit; // Total times code can be used, null for unlimited
    private Integer timesUsed; // How many times it has been used so far

    @Column(nullable = false)
    private boolean active;

    // JPA requires a public or protected no-argument constructor
    public Promotion() {}

    // Constructor with eventId
    public Promotion(String code, Long eventId, String type, Double discountValue, LocalDate startDate, LocalDate endDate, Integer usageLimit, boolean active) {
        this.code = code;
        this.eventId = eventId;
        this.type = type;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.timesUsed = 0; // Initialize timesUsed
        this.active = active;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getCode() { return code; }
    public Long getEventId() { return eventId; } // New getter
    public String getType() { return type; }
    public Double getDiscountValue() { return discountValue; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Integer getUsageLimit() { return usageLimit; }
    public Integer getTimesUsed() { return timesUsed; }
    public boolean isActive() { return active; }

    // --- Setters ---
    public void setCode(String code) { this.code = code; }
    public void setEventId(Long eventId) { this.eventId = eventId; } // New setter
    public void setType(String type) { this.type = type; }
    public void setDiscountValue(Double discountValue) { this.discountValue = discountValue; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
    public void setTimesUsed(Integer timesUsed) { this.timesUsed = timesUsed; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", eventId=" + eventId +
                ", type='" + type + '\'' +
                ", discountValue=" + discountValue +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", usageLimit=" + usageLimit +
                ", timesUsed=" + timesUsed +
                ", active=" + active +
                '}';
    }
}