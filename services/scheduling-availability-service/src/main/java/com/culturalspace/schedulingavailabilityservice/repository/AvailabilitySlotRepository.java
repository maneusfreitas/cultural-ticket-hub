package com.culturalspace.schedulingavailabilityservice.repository;

import com.culturalspace.schedulingavailabilityservice.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    // Custom query: Find slots for a specific event
    List<AvailabilitySlot> findByEventId(Long eventId);

    // Custom query: Find slots within a date range
    List<AvailabilitySlot> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Custom query: Find slots by event and location
    List<AvailabilitySlot> findByEventIdAndLocation(Long eventId, String location);
}