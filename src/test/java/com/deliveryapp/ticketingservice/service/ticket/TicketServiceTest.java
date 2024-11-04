package com.deliveryapp.ticketingservice.service.ticket;

import com.deliveryapp.ticketingservice.constants.TicketStatus;
import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.repository.ticket.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;


import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.DeliveryStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.repository.delivery.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket1;
    private Ticket ticket2;


    List<Ticket> ticketList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        ticketService = new TicketService(ticketRepository, cacheManager);

        ticket1 = Ticket.builder()
                .priority(3)
                .status(TicketStatus.CREATED)
                .deliveryId(5L)
                .build();

        ticket2 = Ticket.builder()
                .priority(2)
                .status(TicketStatus.CREATED)
                .deliveryId(6L)
                .build();

        Collections.addAll(ticketList, ticket1, ticket2);

    }


    @Test
    void testGetAllTickets_thenAllDeliveriesFetched() {
        when(ticketRepository.findAll()).thenReturn(ticketList);
        List<Ticket> fetchedDeliveries = ticketService.getAllTickets();
        assertThat(fetchedDeliveries.size()).isGreaterThan(0);
    }

    @Test
    public void testGetAllTickets() {

        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket1, ticket2));

        List<Ticket> tickets = ticketService.getAllTickets();

        assertNotNull(tickets);
        assertEquals(2, tickets.size());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    public void testGetTicketByDeliveryId() {

        when(ticketRepository.findByDeliveryId(5L)).thenReturn(Optional.of(ticket1));
        Optional<Ticket> result = ticketService.getTicketByDeliveryId(5L);

        assertTrue(result.isPresent());
        assertEquals(ticket1, result.get());
        verify(ticketRepository, times(1)).findByDeliveryId(5L);
    }

    @Test
    public void testPersistTicket() {

        ticketService.persistTicket(ticket1);

        verify(ticketRepository, times(1)).save(ticket1);
    }

    @Test
    public void testClearTicketCache() {

        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("tickets")).thenReturn(cache);

        ticketService.clearTicketCache(ticket1.getId());

        verify(cache, times(1)).evict(ticket1.getId());
    }

    @Test
    public void testFallbackTicketResponse() {

        String response = ticketService.fallbackTicketResponse(new RuntimeException("Failure caused"));

        assertEquals("Something went wrong, please try again later.", response);
    }

}



