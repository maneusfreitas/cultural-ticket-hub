package com.culturalspace.bookingservice.service;

import com.culturalspace.bookingservice.event.BookingConfirmedEvent;
import com.culturalspace.bookingservice.model.Booking;
import com.culturalspace.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final KafkaTemplate<String, BookingConfirmedEvent> kafkaTemplate;
    private final BookingRepository bookingRepository;
    private final WebClient webClient;

    @Value("${services.scheduling-availability-service.url}")
    private String schedulingAvailabilityServiceUrl;

    @Value("${services.seat-reservation-service.url}")
    private String seatReservationServiceUrl;

    @Value("${services.payment-processing-service.url}")
    private String paymentProcessingServiceUrl;


    public BookingService(KafkaTemplate<String, BookingConfirmedEvent> kafkaTemplate, BookingRepository bookingRepository, WebClient.Builder webClientBuilder) {
        this.bookingRepository = bookingRepository;
        this.webClient = webClientBuilder.build();
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Booking createBooking(Long userId, Long availabilitySlotId, Integer numberOfSeats, BigDecimal totalAmount) {
        Booking booking = new Booking(userId, availabilitySlotId, numberOfSeats, totalAmount, LocalDateTime.now(), "PENDING");
        Booking savedBooking = bookingRepository.save(booking);
        System.out.println("BookingService: Initiated booking " + savedBooking.getId() + " for slot " + availabilitySlotId);

        Long bookingId = savedBooking.getId();

        try {
            // --- Step 1: Decrease seats in Scheduling-Availability-Service ---
            System.out.println("BookingService: Calling Scheduling-Availability-Service to decrease seats for slot " + availabilitySlotId);
            webClient.post()
                    .uri(schedulingAvailabilityServiceUrl + "/availability-slots/{id}/decrease-seats?numSeats={numSeats}",
                            availabilitySlotId, numberOfSeats)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> {
                        response.bodyToMono(String.class).subscribe(body -> System.err.println("Client Error from Scheduling-Availability-Service: " + body));
                        return Mono.error(new RuntimeException("Not enough seats available or invalid slot (4xx error) from Scheduling-Availability-Service. Status: " + response.statusCode()));
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        response.bodyToMono(String.class).subscribe(body -> System.err.println("Server Error from Scheduling-Availability-Service: " + body));
                        return Mono.error(new RuntimeException("Server Error (5xx) from Scheduling-Availability-Service. Status: " + response.statusCode()));
                    })
                    .bodyToMono(Void.class)
                    .block();

            // --- Step 2: Create Reservation in Seat-Reservation-Service ---
            System.out.println("BookingService: Calling Seat-Reservation-Service to create reservation for slot " + availabilitySlotId);
            ReservationResponse reservationResponse = webClient.post()
                    .uri(seatReservationServiceUrl + "/reservations/book")
                    .bodyValue(new SeatReservationRequest(availabilitySlotId, userId, numberOfSeats, totalAmount))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        response.bodyToMono(String.class).subscribe(body -> System.err.println("Client Error from Seat-Reservation-Service: " + body));
                        return Mono.error(new RuntimeException("Failed to create reservation record (4xx error) from Seat-Reservation-Service. Status: " + response.statusCode()));
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        response.bodyToMono(String.class).subscribe(body -> System.err.println("Server Error from Seat-Reservation-Service: " + body));
                        return Mono.error(new RuntimeException("Server Error (5xx) from Seat-Reservation-Service. Status: " + response.statusCode()));
                    })
                    .bodyToMono(ReservationResponse.class)
                    .block();

            savedBooking.setReservationId(reservationResponse.getId());
            savedBooking = bookingRepository.save(savedBooking);

            // --- Step 3: Process Payment in Payment-Processing-Service ---
            System.out.println("BookingService: Calling Payment-Processing-Service to process payment for booking " + bookingId);
            PaymentResponse paymentResponse = webClient.post()
                    .uri(paymentProcessingServiceUrl + "/payments/process")
                    .bodyValue(new PaymentRequest(bookingId, totalAmount, "CARD"))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        response.bodyToMono(String.class).subscribe(body -> System.err.println("Client Error from Payment-Processing-Service: " + body));
                        return Mono.error(new RuntimeException("Payment failed (4xx error) from Payment-Processing-Service. Status: " + response.statusCode()));
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        response.bodyToMono(String.class).subscribe(body -> System.err.println("Server Error from Payment-Processing-Service: " + body));
                        return Mono.error(new RuntimeException("Server Error (5xx) from Payment-Processing-Service. Status: " + response.statusCode()));
                    })
                    .bodyToMono(PaymentResponse.class)
                    .block();

            if (!"SUCCESS".equals(paymentResponse.getStatus())) {
                throw new RuntimeException("Payment was not successful. Status: " + paymentResponse.getStatus());
            }

            savedBooking.setPaymentTransactionId(paymentResponse.getTransactionId());
            savedBooking.setStatus("CONFIRMED");
            savedBooking = bookingRepository.save(savedBooking);
            System.out.println("BookingService: Booking " + bookingId + " CONFIRMED successfully!");

            // --- Step 4: Send Notification (New Integration) ---
            System.out.println("BookingService: Calling Notification-Service to send confirmation for booking " + bookingId);
            try {

                // 1. Create the Kafka event object
                BookingConfirmedEvent event = new BookingConfirmedEvent(
                        bookingId,
                        userId,
                        numberOfSeats,
                        totalAmount
                );

                // 2. Send the event to Kafka asynchronously
                // "booking-confirmed-topic" is the Kafka topic name.
                // event.getBookingId().toString() is the message key (for partitioning).
                // 'event' is the message value (your DTO).
                kafkaTemplate.send("booking-confirmed-topic", event.getBookingId().toString(), event)
                        .whenComplete((result, ex) -> {
                            if (ex != null) {
                                // In a real application, you would handle this failure (e.g., retry, dead-letter queue)
                                // As per your request, no logging here.
                            }
                        });

                System.out.println("BookingService: Confirmation notification sent for booking " + bookingId);

            } catch (RuntimeException e) {
                System.err.println("BookingService: Failed to send notification for booking " + bookingId + ": " + e.getMessage());
                // IMPORTANT: A notification failure should generally NOT rollback the booking.
                // It should be logged and potentially retried via a separate mechanism (e.g., message queue).
                // For this example, we'll just log and continue the main booking flow.
            }

            return savedBooking;

        } catch (WebClientResponseException e) {
            System.err.println("BookingService: WebClient error during booking " + bookingId + ": " + e.getMessage());
            handleBookingFailure(bookingId, e.getMessage());
            throw new RuntimeException("Booking process failed due to external service error: " + e.getResponseBodyAsString(), e);
        } catch (RuntimeException e) {
            System.err.println("BookingService: Runtime error during booking " + bookingId + ": " + e.getMessage());
            handleBookingFailure(bookingId, e.getMessage());
            throw new RuntimeException("Booking process failed: " + e.getMessage(), e);
        }
    }

    private void handleBookingFailure(Long bookingId, String reason) {
        bookingRepository.findById(bookingId).ifPresent(booking -> {
            booking.setStatus("FAILED");
            bookingRepository.save(booking);
            System.err.println("Booking " + bookingId + " failed. Reason: " + reason);
            System.out.println("BookingService: Compensating transactions *not* implemented for this simplified example.");
            System.out.println("BookingService: You would need to add logic here to revert changes made in other services.");
        });
    }

    public static class CreateBookingRequest { /* ... (unchanged) ... */
        private Long userId;
        private Long availabilitySlotId;
        private Integer numberOfSeats;
        private BigDecimal totalAmount;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getAvailabilitySlotId() { return availabilitySlotId; }
        public void setAvailabilitySlotId(Long availabilitySlotId) { this.availabilitySlotId = availabilitySlotId; }
        public Integer getNumberOfSeats() { return numberOfSeats; }
        public void setNumberOfSeats(Integer numberOfSeats) { this.numberOfSeats = numberOfSeats; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    }

    private static class SeatReservationRequest { /* ... (unchanged) ... */
        private Long availabilitySlotId;
        private Long userId;
        private Integer numberOfSeats;
        private BigDecimal totalPrice;

        public SeatReservationRequest(Long availabilitySlotId, Long userId, Integer numberOfSeats, BigDecimal totalPrice) {
            this.availabilitySlotId = availabilitySlotId;
            this.userId = userId;
            this.numberOfSeats = numberOfSeats;
            this.totalPrice = totalPrice;
        }

        public Long getAvailabilitySlotId() { return availabilitySlotId; }
        public Long getUserId() { return userId; }
        public Integer getNumberOfSeats() { return numberOfSeats; }
        public BigDecimal getTotalPrice() { return totalPrice; }
    }

    private static class ReservationResponse { /* ... (unchanged) ... */
        private Long id;
        private String status;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    private static class PaymentRequest { /* ... (unchanged) ... */
        private Long bookingId;
        private BigDecimal amount;
        private String paymentMethod;

        public PaymentRequest(Long bookingId, BigDecimal amount, String paymentMethod) {
            this.bookingId = bookingId;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
        }

        public Long getBookingId() { return bookingId; }
        public BigDecimal getAmount() { return amount; }
        public String getPaymentMethod() { return paymentMethod; }
    }

    private static class PaymentResponse { /* ... (unchanged) ... */
        private String transactionId;
        private String status;
        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // New DTO for Notification Service (defined as a nested class for simplicity)
    private static class NotificationRequest {
        private String recipient;
        private String subject;
        private String message;

        public NotificationRequest(String recipient, String subject, String message) {
            this.recipient = recipient;
            this.subject = subject;
            this.message = message;
        }

        public String getRecipient() { return recipient; }
        public void setRecipient(String recipient) { this.recipient = recipient; }
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }


    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(Long id) {
        System.out.println("BookingService: Fetching booking with ID: " + id);
        return bookingRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        System.out.println("BookingService: Fetching all bookings.");
        return bookingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByUserId(Long userId) {
        System.out.println("BookingService: Fetching bookings for user ID: " + userId);
        return bookingRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByAvailabilitySlotId(Long slotId) {
        System.out.println("BookingService: Fetching bookings for availability slot ID: " + slotId);
        return bookingRepository.findByAvailabilitySlotId(slotId);
    }
}