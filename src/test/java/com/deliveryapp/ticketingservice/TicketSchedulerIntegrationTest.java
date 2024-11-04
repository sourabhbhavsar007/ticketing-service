package com.deliveryapp.ticketingservice.integration;

import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.DeliveryStatus;
import com.deliveryapp.ticketingservice.constants.TicketStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.repository.delivery.DeliveryRepository;
import com.deliveryapp.ticketingservice.repository.ticket.TicketRepository;
import com.deliveryapp.ticketingservice.scheduler.TicketScheduler;
import com.deliveryapp.ticketingservice.service.delivery.DeliveryService;
import com.deliveryapp.ticketingservice.service.ticket.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TicketSchedulerIntegrationTest {

    @Autowired
    private TicketScheduler ticketScheduler;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private TicketService ticketService;

    @BeforeEach
    public void setup() {
        ticketRepository.deleteAll();
        deliveryRepository.deleteAll();
    }

    @Test
    public void testMonitorDeliveries_withValidDelivery() {

        Delivery delivery = Delivery.builder()
                .customerType(CustomerType.VIP)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(3.4f)
                .expectedDeliveryTime(LocalDateTime.now().minusMinutes(10))
                .riderRating(9.3f)
                .meanPreparationTime(35)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(15))
                .isTicketCreated("FALSE")
                .build();

        Ticket existingTicket = new Ticket();
        existingTicket.setDeliveryId(delivery.getId());
        existingTicket.setStatus(TicketStatus.CREATED);
        ticketRepository.save(existingTicket);


        ticketScheduler.monitorDeliveries();

        List<Ticket> tickets = ticketRepository.findAll();
        assertEquals(1, tickets.size());
    }

    @Test
    public void testMonitorDeliveries_withNoDeliveriesNoTicketCreated() {
        ticketScheduler.monitorDeliveries();

        List<Ticket> tickets = ticketRepository.findAll();
        assertTrue(tickets.isEmpty(), "No tickets should be created if no valid deliveries are present");
    }
}
