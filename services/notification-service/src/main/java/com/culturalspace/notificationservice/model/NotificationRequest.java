// src/main/java/com/culturalspace/notificationservice/model/NotificationRequest.java
package com.culturalspace.notificationservice.model;

// This DTO represents the structure of the incoming notification request
public class NotificationRequest {
    private String recipient; // e.g., email address, phone number
    private String subject;   // For email notifications
    private String message;   // The content of the notification

    // Getters and Setters (important for Spring to map JSON to this object)
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}