package com.deliveryapp.ticketingservice.service.ticket;

import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.repository.ticket.TicketRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
    private TicketRepository ticketRepository;

    public List<Ticket> getAllTickets() {
        logger.info("Fetching all the tickets...");
        return ticketRepository.findAll();
    }
}
