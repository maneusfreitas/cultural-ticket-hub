// src/main/java/com/culturalspace/notificationservice/controller/NotificationController.java
package com.culturalspace.notificationservice.controller;

import com.culturalspace.notificationservice.model.NotificationRequest;
import com.culturalspace.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications") // Base path for notification-related endpoints
public class NotificationController {

    private final NotificationService notificationService;

    // Spring will automatically inject NotificationService here
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send") // Handles POST requests to /notifications/send
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        // Call the service to send the notification
        notificationService.sendNotification(request);
        // Return a successful response
        return ResponseEntity.ok("Notification sent successfully (simulated).");
    }
}