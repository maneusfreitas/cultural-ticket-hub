package com.culturalspace.seatreservationservice.controller;

import com.culturalspace.seatreservationservice.model.Reservation;
import com.culturalspace.seatreservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/book")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        try {
            Reservation newReservation = reservationService.createReservation(reservation);
            return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Controller error during reservation creation: " + e.getMessage());
            // Return BAD_REQUEST (400) for client-side errors, or INTERNAL_SERVER_ERROR (500) for unexpected issues
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        try {
            Reservation cancelledReservation = reservationService.cancelReservation(id);
            return ResponseEntity.ok(cancelledReservation);
        } catch (RuntimeException e) {
            System.err.println("Controller error during reservation cancellation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Long userId) {
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/by-slot/{slotId}")
    public ResponseEntity<List<Reservation>> getReservationsByAvailabilitySlotId(@PathVariable Long slotId) {
        List<Reservation> reservations = reservationService.getReservationsByAvailabilitySlotId(slotId);
        return ResponseEntity.ok(reservations);
    }
}