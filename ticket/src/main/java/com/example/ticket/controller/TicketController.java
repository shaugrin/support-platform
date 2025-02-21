package com.example.ticket.controller;

import com.example.ticket.dto.TicketRequest;
import com.example.ticket.dto.TicketResponse;
import com.example.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // Create a new ticket (called by a customer)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(@RequestBody TicketRequest ticketRequest) {
        return ticketService.createTicket(ticketRequest);
    }

    // Assign a ticket to an agent (called by an admin/agent)
    @PatchMapping("/{ticketId}/assign/{agentId}")
    public TicketResponse assignTicket(
            @PathVariable Long ticketId,
            @PathVariable Long agentId
    ) {
        return ticketService.assignTicket(ticketId, agentId);
    }

    // Get all tickets
    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }
}