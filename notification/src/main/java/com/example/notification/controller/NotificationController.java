package com.example.notification.controller;

import com.example.notification.dto.NotificationRequest;
import com.example.notification.service.EmailService;
import com.example.notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;
    private final SmsService smsService;

    @PostMapping("/email")
    public void sendEmailNotification(@RequestBody NotificationRequest request) {
        emailService.sendEmail(
                request.getEmail(),
                "Support Platform Update",
                request.getMessage()
        );
    }

    @PostMapping("/sms")
    public void sendSmsNotification(@RequestBody NotificationRequest request) {
        smsService.sendSms(request.getPhone(), request.getMessage());
    }
}
