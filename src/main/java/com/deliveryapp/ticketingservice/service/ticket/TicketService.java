package com.deliveryapp.ticketingservice.service.ticket;

import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.repository.ticket.TicketRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
    private TicketRepository ticketRepository;
    private CacheManager cacheManager;


    @CircuitBreaker(name = "ticketCircuitBreaker", fallbackMethod = "fallbackTicketResponse")
    @Cacheable("tickets")
    public List<Ticket> getAllTickets() {
        logger.info("Fetching all the tickets...");
        return ticketRepository.findAll();
    }


    @CircuitBreaker(name = "ticketCircuitBreaker", fallbackMethod = "fallbackTicketResponse")
    @Cacheable("ticket")
    public Optional<Ticket> getTicketByDeliveryId(Long deliveryId) {
        logger.info("Fetching the ticket by delivery id: {}", deliveryId);
        return ticketRepository.findByDeliveryId(deliveryId);
    }

    @CacheEvict(value = "tickets", allEntries = true)
    public void persistTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    //Normally we will call cache evict on update or delete operations
    public void clearTicketCache(Long ticketId) {
        Objects.requireNonNull(cacheManager.getCache("tickets")).evict(ticketId);
    }

    public String fallbackTicketResponse(Throwable t) {
        return "Something went wrong, please try again later.";
    }

}
