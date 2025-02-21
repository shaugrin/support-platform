package com.example.ticket.dto;

import com.example.ticket.model.Priority;
import com.example.ticket.model.TicketStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private Priority priority;
    private Long createdBy;
    private Long assignedTo;
    private LocalDateTime createdAt;
}
