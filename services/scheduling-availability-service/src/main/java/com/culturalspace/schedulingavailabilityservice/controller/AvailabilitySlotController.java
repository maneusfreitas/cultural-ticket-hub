package com.culturalspace.schedulingavailabilityservice.controller;

import com.culturalspace.schedulingavailabilityservice.model.AvailabilitySlot;
import com.culturalspace.schedulingavailabilityservice.service.AvailabilitySlotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/availability-slots") // Base path for all availability slot endpoints
public class AvailabilitySlotController {

    private final AvailabilitySlotService availabilitySlotService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AvailabilitySlotController(AvailabilitySlotService availabilitySlotService) {
        this.availabilitySlotService = availabilitySlotService;
    }

    @PostMapping("/add")
    public ResponseEntity<AvailabilitySlot> createAvailabilitySlot(@RequestBody AvailabilitySlot slot) {
        AvailabilitySlot newSlot = availabilitySlotService.createAvailabilitySlot(slot);
        return new ResponseEntity<>(newSlot, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilitySlot> getAvailabilitySlotById(@PathVariable Long id) {
        return availabilitySlotService.getAvailabilitySlotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AvailabilitySlot>> getAllAvailabilitySlots() {
        List<AvailabilitySlot> slots = availabilitySlotService.getAllAvailabilitySlots();
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/by-event/{eventId}")
    public ResponseEntity<List<AvailabilitySlot>> getAvailabilitySlotsByEventId(@PathVariable Long eventId) {
        List<AvailabilitySlot> slots = availabilitySlotService.getAvailabilitySlotsByEventId(eventId);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/between-dates")
    public ResponseEntity<List<AvailabilitySlot>> getAvailabilitySlotsBetweenDates(
            @RequestParam("start") String startDateStr,
            @RequestParam("end") String endDateStr) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDateStr, DATE_TIME_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(endDateStr, DATE_TIME_FORMATTER);
            List<AvailabilitySlot> slots = availabilitySlotService.getAvailabilitySlotsBetweenDates(start, end);
            return ResponseEntity.ok(slots);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null); // Or return a custom error message
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<AvailabilitySlot> updateAvailabilitySlot(@PathVariable Long id, @RequestBody AvailabilitySlot slot) {
        try {
            AvailabilitySlot updatedSlot = availabilitySlotService.updateAvailabilitySlot(id, slot);
            return ResponseEntity.ok(updatedSlot);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAvailabilitySlot(@PathVariable Long id) {
        availabilitySlotService.deleteAvailabilitySlot(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/decrease-seats")
    public ResponseEntity<AvailabilitySlot> decreaseAvailableSeats(
            @PathVariable Long id,
            @RequestParam int numSeats) {
        try {
            AvailabilitySlot updatedSlot = availabilitySlotService.decreaseAvailableSeats(id, numSeats);
            return ResponseEntity.ok(updatedSlot);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Or more specific error based on exception message
        }
    }

    @PostMapping("/{id}/increase-seats")
    public ResponseEntity<AvailabilitySlot> increaseAvailableSeats(
            @PathVariable Long id,
            @RequestParam int numSeats) {
        try {
            AvailabilitySlot updatedSlot = availabilitySlotService.increaseAvailableSeats(id, numSeats);
            return ResponseEntity.ok(updatedSlot);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Or more specific error
        }
    }
}