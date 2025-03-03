package com.example.notification.service;

import com.example.notification.client.UserServiceClient;
import com.example.notification.dto.UserResponse;
import com.example.ticket.event.TicketAssignedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private static final Logger logger = LogManager.getLogger(NotificationConsumer.class);

    private final UserServiceClient userServiceClient;
    private final EmailService emailService;
    private final SmsService smsService;

    @KafkaListener(topics = "ticket-assigned", groupId = "notification-group")
    public void handleTicketAssigned(TicketAssignedEvent event) {
        logger.info("Received TicketAssignedEvent: Agent ID={}, Message={}",
                event.getAgentId(), event.getMessage());

        try {
            UserResponse agent = userServiceClient.getUserById(event.getAgentId());
            logger.info("Fetched agent details: Email={}, Phone={}", agent.getEmail(), agent.getPhone());

            emailService.sendEmail(agent.getEmail(), "New Ticket Assigned", event.getMessage());
            logger.info("Email sent to {}", agent.getEmail());

            smsService.sendSms(agent.getPhone(), event.getMessage());
            logger.info("SMS sent to {}", agent.getPhone());
        } catch (Exception e) {
            logger.error("Error handling TicketAssignedEvent", e);
        }
    }
}
