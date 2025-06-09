// src/main/java/com/culturalspace/notificationservice/service/NotificationService.java
package com.culturalspace.notificationservice.service;

import com.culturalspace.notificationservice.model.NotificationRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    public void sendNotification(NotificationRequest request) {
        // In a real-world application, this method would integrate with an
        // actual email service (e.g., SendGrid, Mailgun), SMS gateway (Twilio),
        // or a push notification provider.

        System.out.println("--------------------------------------------------");
        System.out.println("NotificationService: Sending notification at " + LocalDateTime.now());
        System.out.println("  Recipient: " + request.getRecipient());
        System.out.println("  Subject: " + request.getSubject());
        System.out.println("  Message: " + request.getMessage());
        System.out.println("Notification sent successfully (simulated).");
        System.out.println("--------------------------------------------------");

        // You might also save notification logs to a database here
        // (e.g., notificationRepository.save(new NotificationLog(...)))
    }
}