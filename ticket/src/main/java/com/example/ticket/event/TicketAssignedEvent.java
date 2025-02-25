// TicketAssignedEvent.java
package com.example.ticket.event;

import lombok.Data;

@Data
public class TicketAssignedEvent {
    private Long ticketId;
    private Long agentId;
    private String message;
}