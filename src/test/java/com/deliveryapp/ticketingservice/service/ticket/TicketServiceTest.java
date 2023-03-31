package com.deliveryapp.ticketingservice.service.ticket;

import com.deliveryapp.ticketingservice.constants.TicketStatus;
import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.repository.ticket.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    TicketService ticketService;

    List<Ticket> ticketList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        ticketService = new TicketService(ticketRepository);

        ticketList.add(Ticket.builder()
                .priority(3)
                .status(TicketStatus.CREATED)
                .deliveryId(5l)
                .build());

    }


    @Test
    void testGetAllTickets_thenAllDeliveriesFetched() {

        when(ticketRepository.findAll()).thenReturn(ticketList);

        List<Ticket> fetchedDeliveries = ticketService.getAllTickets();

        assertThat(fetchedDeliveries.size()).isGreaterThan(0);
    }

}
