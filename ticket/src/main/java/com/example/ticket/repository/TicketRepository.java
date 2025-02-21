package com.example.ticket.repository;

import com.example.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByAssignedTo(Long assignedTo); // For fetching agent-specific tickets
}