package com.culturalspace.eventcatalogservice.controller;

import com.culturalspace.eventcatalogservice.model.Event; // Referencing the 'Event' model
import com.culturalspace.eventcatalogservice.service.EventService; // Referencing the 'EventService'
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST controller
@RequestMapping("/events") // Base path for all event-related endpoints (simpler now)
public class EventController {

    private final EventService eventService; // Inject the service

    // Constructor Injection
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create-event") // Handles POST requests to /events
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event newEvent = eventService.createEvent(event);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED); // Returns 201 Created status
    }

    @GetMapping("/{id}") // Handles GET requests to /events/{id}
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok) // If event found, return 200 OK with event data
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    @GetMapping // Handles GET requests to /events
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events); // Returns 200 OK with list of events
    }

    @PutMapping("/edit-event/{id}") // Handles PUT requests to /events/{id}
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        try {
            Event updatedEvent = eventService.updateEvent(id, event);
            return ResponseEntity.ok(updatedEvent); // Returns 200 OK with updated event
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @DeleteMapping("/delete-event/{id}") // Handles DELETE requests to /events/{id}
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }
}