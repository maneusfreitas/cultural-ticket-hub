package com.culturalspace.seatreservationservice.service;

import com.culturalspace.seatreservationservice.model.Reservation;
import com.culturalspace.seatreservationservice.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        reservation.setReservationTime(LocalDateTime.now());
        // In this simplified version, we'll mark it as CONFIRMED directly.
        // In a real system, this would be tied to external availability checks and payment.
        reservation.setStatus("CONFIRMED");
        System.out.println("ReservationService: Creating reservation for slot " + reservation.getAvailabilitySlotId() + ". Status: CONFIRMED.");
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(reservation -> {
                    if ("CONFIRMED".equals(reservation.getStatus())) {
                        reservation.setStatus("CANCELLED");
                        System.out.println("ReservationService: Cancelling reservation " + reservation.getId() + ". Status: CANCELLED.");
                        return reservationRepository.save(reservation);
                    } else {
                        throw new RuntimeException("Reservation " + reservationId + " cannot be cancelled as its status is " + reservation.getStatus());
                    }
                })
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + reservationId));
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(Long id) {
        System.out.println("ReservationService: Fetching reservation with ID: " + id);
        return reservationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        System.out.println("ReservationService: Fetching all reservations.");
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByUserId(Long userId) {
        System.out.println("ReservationService: Fetching reservations for user ID: " + userId);
        return reservationRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByAvailabilitySlotId(Long slotId) {
        System.out.println("ReservationService: Fetching reservations for availability slot ID: " + slotId);
        return reservationRepository.findByAvailabilitySlotId(slotId);
    }
}