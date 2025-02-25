package com.example.ticket.client;

import com.example.ticket.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "NOTIFICATION")
public interface NotificationClient {
    @PostMapping("/api/notifications/email")
    void sendEmailNotification(NotificationRequest request);

    @PostMapping("/api/notifications/sms")
    void sendSmsNotification(NotificationRequest request);
}