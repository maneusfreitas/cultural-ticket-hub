package com.culturalspace.eventcatalogservice.model;

import jakarta.persistence.Column; // Make sure this import is present
import jakarta.persistence.Entity; // Make sure this import is present
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // Make sure this import is present

@Entity // <--- THIS IS CRUCIAL! Tells JPA/Hibernate this is a database entity
@Table(name = "event_entry") // Custom table name, good practice to avoid conflicts if 'event' is a keyword
public class Event {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments ID for PostgreSQL
    private Long id;

    @Column(nullable = false, unique = true) // Event name cannot be null and must be unique
    private String name;

    @Column(columnDefinition = "TEXT") // Allows for longer descriptions
    private String description;

    @Column(nullable = false)
    private String category; // e.g., "Music", "Theatre", "Art", "Dance"

    @Column // Optional: Typical duration in minutes
    private Integer typicalDurationMinutes;

    // --- Constructors ---
    // JPA REQUIRES a no-argument constructor for entity instantiation
    public Event() {}

    // Constructor for creating new Event instances (without ID, as it's auto-generated)
    public Event(String name, String description, String category, Integer typicalDurationMinutes) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.typicalDurationMinutes = typicalDurationMinutes;
    }

    // --- Getters (one-liner format) ---
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Integer getTypicalDurationMinutes() { return typicalDurationMinutes; }

    // --- Setters (one-liner format) ---
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setTypicalDurationMinutes(Integer typicalDurationMinutes) { this.typicalDurationMinutes = typicalDurationMinutes; }

    // --- toString() ---
    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", category='" + category + '\'' + ", typicalDurationMinutes=" + typicalDurationMinutes + '}';
    }

    // --- equals() and hashCode() (Highly Recommended for JPA Entities) ---
    // Critical for proper functioning in collections and comparisons.
    // For entities, equality should generally be based on the ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id != null && id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}