package com.culturalspace.notificationservice.service;

import com.culturalspace.notificationservice.event.BookingConfirmedEvent;
import com.culturalspace.notificationservice.model.NotificationLog; // Import your NotificationLog entity
import com.culturalspace.notificationservice.repository.NotificationRepository; // Import your repository
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // For logging the timestamp

@Service
public class NotificationService {

    private final NotificationRepository notificationLogRepository; // Inject the repository

    // Spring will automatically inject NotificationLogRepository here
    public NotificationService(NotificationRepository notificationLogRepository) {
        this.notificationLogRepository = notificationLogRepository;
    }

    /**
     * This method acts as a Kafka consumer, listening for BookingConfirmedEvent messages.
     * When a message is published to the "booking-confirmed-topic", Spring Kafka
     * automatically deserializes the message and invokes this method.
     *
     * @param event The deserialized BookingConfirmedEvent object containing confirmed booking details.
     */
    @KafkaListener(topics = "booking-confirmed-topic", groupId = "notification-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(BookingConfirmedEvent event) {
        // Outputting the received event details to the console
        System.out.println("----------------------------------------------------");
        System.out.println("NotificationService: Received Booking Confirmed Event from Kafka!");
        System.out.println("  Booking ID: " + event.getBookingId());
        System.out.println("  User ID: " + event.getUserId()); // Assuming you have getUserId()
        System.out.println("  Number of Seats: " + event.getNumberOfSeats());
        System.out.println("  Total Price: " + event.getTotalPrice()); // Assuming you have getTotalPrice()
        // Ensure your BookingConfirmedEvent has getters for all these fields,
        // and also for userEmail, eventName, eventDateTime which are used below for NotificationLog.
        System.out.println("----------------------------------------------------");

        // --- NEW: Save the Kafka message data to the database ---
        try {
            // Create a new NotificationLog object from the event data
            NotificationLog notificationLog = new NotificationLog(
                    event.getBookingId(),
                    event.getUserId(),
                    LocalDateTime.now() // Timestamp when this log entry is created
            );

            // Save the log entry using the repository
            NotificationLog savedLog = notificationLogRepository.save(notificationLog);
            System.out.println("NotificationService: Kafka message logged to DB. Log ID: " + savedLog.getId());

        } catch (Exception e) {
            // Log any errors that occur during the database save operation
            System.err.println("NotificationService: Failed to save notification log to database: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for detailed debugging
        }

        // --- Placeholder for actual notification logic (e.g., sending email) ---
        // emailService.sendBookingConfirmation(event.getUserEmail(), event.getEventName(), event.getBookingId(), event.getEventDateTime());
    }
}