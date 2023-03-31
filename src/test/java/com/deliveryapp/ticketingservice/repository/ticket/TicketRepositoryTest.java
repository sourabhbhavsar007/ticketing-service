package com.deliveryapp.ticketingservice.repository.ticket;

import com.deliveryapp.ticketingservice.TicketingServiceApplication;
import com.deliveryapp.ticketingservice.constants.TicketStatus;
import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.repository.ticket.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketRepositoryTest {
    @Autowired
    TicketRepository ticketRepository;

    @BeforeEach
    void setUp(){
        ticketRepository.save(Ticket.builder()
                .priority(1)
                .status(TicketStatus.CREATED)
                .deliveryId(3l)
                .build());

        ticketRepository.save(Ticket.builder()
                .priority(2)
                .status(TicketStatus.CREATED)
                .deliveryId(4l)
                .build());

        ticketRepository.save(Ticket.builder()
                .priority(3)
                .status(TicketStatus.CREATED)
                .deliveryId(5l)
                .build());

    }

    @AfterEach
    void tearDown(){
        ticketRepository.deleteAll();
    }


    @Test
    void testGivenList_whenGettingAllTickets_thenGetAllTickets() {

        List<Ticket> resultList = ticketRepository.findAll();

        assertThat(resultList.size()).isGreaterThan(0);
    }

    @Test
    void testGivenDeliveryId_whenGettingTickets_thenGetTicketForGivenDeliveryId() {

        Optional<Ticket> searchedTicket = ticketRepository.findByDeliveryId(3l);

        assertThat(searchedTicket.get().getStatus().equals(TicketStatus.CREATED));
    }

}