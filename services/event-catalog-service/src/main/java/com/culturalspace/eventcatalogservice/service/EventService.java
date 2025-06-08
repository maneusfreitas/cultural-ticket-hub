package com.culturalspace.eventcatalogservice.service;

import com.culturalspace.eventcatalogservice.model.Event; // Referencing the 'Event' model
import com.culturalspace.eventcatalogservice.repository.EventRepository; // Referencing the 'EventRepository'
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring Service component
public class EventService {

    private final EventRepository eventRepository; // Inject the repository

    // Constructor Injection
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event createEvent(Event event) {
        System.out.println("EventService: Creating event: " + event.getName());
        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Optional<Event> getEventById(Long id) {
        System.out.println("EventService: Fetching event with ID: " + id);
        return eventRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Event> getEventByName(String name) {
        System.out.println("EventService: Fetching event with name: " + name);
        return eventRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        System.out.println("EventService: Fetching all events.");
        return eventRepository.findAll();
    }

    @Transactional
    public Event updateEvent(Long id, Event updatedEvent) {
        System.out.println("EventService: Updating event with ID: " + id);
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setName(updatedEvent.getName());
                    existingEvent.setDescription(updatedEvent.getDescription());
                    existingEvent.setCategory(updatedEvent.getCategory());
                    existingEvent.setTypicalDurationMinutes(updatedEvent.getTypicalDurationMinutes());
                    return eventRepository.save(existingEvent);
                })
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));
    }

    @Transactional
    public void deleteEvent(Long id) {
        System.out.println("EventService: Deleting event with ID: " + id);
        eventRepository.deleteById(id);
    }
}