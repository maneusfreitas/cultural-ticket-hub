package com.culturalspace.seatreservationservice.repository;

import com.culturalspace.seatreservationservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByAvailabilitySlotId(Long availabilitySlotId);
    List<Reservation> findByStatus(String status);
}