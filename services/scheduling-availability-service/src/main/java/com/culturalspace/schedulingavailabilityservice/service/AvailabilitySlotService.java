package com.culturalspace.schedulingavailabilityservice.service;

import com.culturalspace.schedulingavailabilityservice.model.AvailabilitySlot;
import com.culturalspace.schedulingavailabilityservice.repository.AvailabilitySlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilitySlotService {

    private final AvailabilitySlotRepository availabilitySlotRepository;

    public AvailabilitySlotService(AvailabilitySlotRepository availabilitySlotRepository) {
        this.availabilitySlotRepository = availabilitySlotRepository;
    }

    @Transactional
    public AvailabilitySlot createAvailabilitySlot(AvailabilitySlot slot) {
        // When creating, set availableSeats to totalSeats initially
        if (slot.getAvailableSeats() == null) {
            slot.setAvailableSeats(slot.getTotalSeats());
        }
        System.out.println("AvailabilitySlotService: Creating slot for event ID: " + slot.getEventId());
        return availabilitySlotRepository.save(slot);
    }

    @Transactional(readOnly = true)
    public Optional<AvailabilitySlot> getAvailabilitySlotById(Long id) {
        System.out.println("AvailabilitySlotService: Fetching slot with ID: " + id);
        return availabilitySlotRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<AvailabilitySlot> getAllAvailabilitySlots() {
        System.out.println("AvailabilitySlotService: Fetching all slots.");
        return availabilitySlotRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AvailabilitySlot> getAvailabilitySlotsByEventId(Long eventId) {
        System.out.println("AvailabilitySlotService: Fetching slots for event ID: " + eventId);
        return availabilitySlotRepository.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<AvailabilitySlot> getAvailabilitySlotsBetweenDates(LocalDateTime start, LocalDateTime end) {
        System.out.println("AvailabilitySlotService: Fetching slots between " + start + " and " + end);
        return availabilitySlotRepository.findByStartTimeBetween(start, end);
    }

    @Transactional
    public AvailabilitySlot updateAvailabilitySlot(Long id, AvailabilitySlot updatedSlot) {
        System.out.println("AvailabilitySlotService: Updating slot with ID: " + id);
        return availabilitySlotRepository.findById(id)
                .map(existingSlot -> {
                    existingSlot.setOperatorId(updatedSlot.getOperatorId());
                    existingSlot.setStartTime(updatedSlot.getStartTime());
                    existingSlot.setEndTime(updatedSlot.getEndTime());
                    existingSlot.setLocation(updatedSlot.getLocation());
                    existingSlot.setTotalSeats(updatedSlot.getTotalSeats());
                    existingSlot.setAvailableSeats(updatedSlot.getAvailableSeats()); // This can be updated by a booking service later
                    return availabilitySlotRepository.save(existingSlot);
                })
                .orElseThrow(() -> new RuntimeException("AvailabilitySlot not found with ID: " + id));
    }

    @Transactional
    public void deleteAvailabilitySlot(Long id) {
        System.out.println("AvailabilitySlotService: Deleting slot with ID: " + id);
        availabilitySlotRepository.deleteById(id);
    }

    // Method to decrease available seats when a ticket is booked (to be used by a future TicketService)
    @Transactional
    public AvailabilitySlot decreaseAvailableSeats(Long slotId, int numSeats) {
        return availabilitySlotRepository.findById(slotId)
                .map(slot -> {
                    if (slot.getAvailableSeats() >= numSeats) {
                        slot.setAvailableSeats(slot.getAvailableSeats() - numSeats);
                        return availabilitySlotRepository.save(slot);
                    } else {
                        throw new RuntimeException("Not enough seats available for slot ID: " + slotId);
                    }
                })
                .orElseThrow(() -> new RuntimeException("AvailabilitySlot not found with ID: " + slotId));
    }

    // Method to increase available seats when a ticket is cancelled
    @Transactional
    public AvailabilitySlot increaseAvailableSeats(Long slotId, int numSeats) {
        return availabilitySlotRepository.findById(slotId)
                .map(slot -> {
                    if (slot.getAvailableSeats() + numSeats <= slot.getTotalSeats()) {
                        slot.setAvailableSeats(slot.getAvailableSeats() + numSeats);
                        return availabilitySlotRepository.save(slot);
                    } else {
                        throw new RuntimeException("Cannot increase seats beyond total capacity for slot ID: " + slotId);
                    }
                })
                .orElseThrow(() -> new RuntimeException("AvailabilitySlot not found with ID: " + slotId));
    }
}