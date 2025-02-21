package com.example.ticket.dto;

import com.example.ticket.model.Priority;
import lombok.Data;

@Data
public class TicketRequest {
    private String title;
    private String description;
    private Priority priority;
    private Long createdBy; // Customer's user ID
}
