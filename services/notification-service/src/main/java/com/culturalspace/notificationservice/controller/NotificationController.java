// src/main/java/com/culturalspace/notificationservice/controller/NotificationController.java
package com.culturalspace.notificationservice.controller;

import com.culturalspace.notificationservice.service.NotificationService;
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
}