package com.example.ticket.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.example.ticket.client.NotificationClient;
import com.example.ticket.client.UserServiceClient;
import com.example.ticket.dto.NotificationRequest;
import com.example.ticket.dto.TicketRequest;
import com.example.ticket.dto.TicketResponse;
import com.example.ticket.dto.UserResponse;
import com.example.ticket.event.TicketAssignedEvent;
import com.example.ticket.exception.*;
import com.example.ticket.model.Ticket;
import com.example.ticket.model.TicketStatus;
import com.example.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserServiceClient userServiceClient;
    private final NotificationClient notificationClient;
    private final KafkaTemplate<String, TicketAssignedEvent> kafkaTemplate;
    private static final Logger logger = LogManager.getLogger(TicketService.class);

    public TicketResponse createTicket(TicketRequest ticketRequest) {
        // Validate that the creator (customer) exists
        UserResponse creator = userServiceClient.getUserById(ticketRequest.getCreatedBy());
        if (creator.getRole().equals("AGENT")) {
            throw new InvalidUserRoleException("Customers must create tickets");
        }

        Ticket ticket = Ticket.builder()
                .title(ticketRequest.getTitle())
                .description(ticketRequest.getDescription())
                .priority(ticketRequest.getPriority())
                .createdBy(ticketRequest.getCreatedBy())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);
        return mapToTicketResponse(savedTicket);
    }

    public TicketResponse assignTicket(Long ticketId, Long agentId) {
        logger.info("Assigning ticket: Ticket ID={}, Agent ID={}", ticketId, agentId);

        // Validate agent exists and has the AGENT role
        UserResponse agent = userServiceClient.getUserById(agentId);
        logger.info("Fetched agent details: Email={}, Role={}", agent.getEmail(), agent.getRole());

        if (!agent.getRole().equals("AGENT")) {
            logger.warn("User with ID={} is not an agent", agentId);
            throw new InvalidUserRoleException("User is not an agent");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    logger.error("Ticket with ID={} not found", ticketId);
                    return new TicketNotFoundException("Ticket not found");
                });

        ticket.setAssignedTo(agentId);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        Ticket updatedTicket = ticketRepository.save(ticket);
        logger.info("Ticket updated: Ticket ID={}, Status={}", ticketId, TicketStatus.IN_PROGRESS);

        // Send Kafka event
        TicketAssignedEvent event = new TicketAssignedEvent();
        event.setTicketId(ticketId);
        event.setAgentId(agentId);
        event.setMessage("Ticket #" + ticketId + " assigned to you");
        kafkaTemplate.send("ticket-assigned", event);
        logger.info("Published Kafka event: Ticket ID={}, Agent ID={}", ticketId, agentId);

        // Send email notification
        NotificationRequest notification = new NotificationRequest();
        notification.setMessage("You’ve been assigned ticket #" + ticketId);
        notification.setEmail(agent.getEmail());
        notificationClient.sendEmailNotification(notification);
        logger.info("Sent email notification to {}", agent.getEmail());

        return mapToTicketResponse(updatedTicket);
    }


    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }

    private TicketResponse mapToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .createdBy(ticket.getCreatedBy())
                .assignedTo(ticket.getAssignedTo())
                .createdAt(ticket.getCreatedAt())
                .build();
    }
}