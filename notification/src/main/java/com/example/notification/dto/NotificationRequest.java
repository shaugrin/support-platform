package com.example.notification.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String message;
    private String email;   // For email notifications
    private String phone;   // For SMS notifications
}