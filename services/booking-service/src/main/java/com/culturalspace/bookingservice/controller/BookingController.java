package com.culturalspace.bookingservice.controller;

import com.culturalspace.bookingservice.model.Booking;
import com.culturalspace.bookingservice.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingService.CreateBookingRequest request) {
        try {
            Booking newBooking = bookingService.createBooking(
                    request.getUserId(),
                    request.getAvailabilitySlotId(),
                    request.getNumberOfSeats(),
                    request.getTotalAmount()
            );
            return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Controller error during booking creation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        /*try {
            Booking cancelledBooking = bookingService.cancelBooking(id);
            return ResponseEntity.ok(cancelledBooking);
        } catch (RuntimeException e) {
            System.err.println("Controller error during booking cancellation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }*/

        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/by-slot/{slotId}")
    public ResponseEntity<List<Booking>> getBookingsByAvailabilitySlotId(@PathVariable Long slotId) {
        List<Booking> bookings = bookingService.getBookingsByAvailabilitySlotId(slotId);
        return ResponseEntity.ok(bookings);
    }
}