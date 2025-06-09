package com.culturalspace.bookingservice.repository;

import com.culturalspace.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByAvailabilitySlotId(Long availabilitySlotId);
    List<Booking> findByStatus(String status);
}