package com.culturalspace.eventcatalogservice.repository;

import com.culturalspace.eventcatalogservice.model.Event; // Referencing the 'Event' model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Marks this interface as a Spring Data JPA repository
public interface EventRepository extends JpaRepository<Event, Long> { // Parameterized with 'Event'
    // JpaRepository provides standard CRUD operations

    // Custom query method: Find an event by its name
    Optional<Event> findByName(String name);

    // Custom query method: Find all events by category
    List<Event> findByCategory(String category);
}