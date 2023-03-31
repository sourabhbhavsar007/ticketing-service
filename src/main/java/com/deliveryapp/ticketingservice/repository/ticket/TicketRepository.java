package com.deliveryapp.ticketingservice.repository.ticket;

import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByDeliveryId(Long id);
}
