package com.deliveryapp.ticketingservice.scheduler;

import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.DeliveryStatus;
import com.deliveryapp.ticketingservice.constants.TicketStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.rule.TicketingRule;
import com.deliveryapp.ticketingservice.service.delivery.DeliveryService;
import com.deliveryapp.ticketingservice.service.ticket.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketSchedulerTest {

    @Mock
    private DeliveryService deliveryService;

    @Mock
    private TicketService ticketService;

    @Mock
    private TicketingRule ticketingRule;

    @InjectMocks
    private TicketScheduler ticketScheduler;

    private Delivery delivery;
    private Ticket ticket;

    @BeforeEach
    void setUp() {

        delivery = Delivery.builder()
                .customerType(CustomerType.VIP)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(3.4f)
                .expectedDeliveryTime(LocalDateTime.now().minusMinutes(10))
                .riderRating(9.3f)
                .meanPreparationTime(35)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(15))
                .isTicketCreated("FALSE")
                .build();

        ticket = Ticket.builder()
                .priority(3)
                .status(TicketStatus.CREATED)
                .deliveryId(5L)
                .build();

        Set<TicketingRule> ticketingRules = new HashSet<>();
        ticketingRules.add(ticketingRule);
        ReflectionTestUtils.setField(ticketScheduler, "ticketingRules", ticketingRules);
    }

    @Test
    public void testMonitorDeliveries_withNoDeliveries() {

        when(deliveryService.getOrdersToBeDelivered()).thenReturn(Collections.emptyList());

        ticketScheduler.monitorDeliveries();

        verify(deliveryService, times(1)).getOrdersToBeDelivered();
        verifyNoMoreInteractions(deliveryService, ticketService, ticketingRule);
    }

    @Test
    public void testMonitorDeliveries_withValidDeliveries() {

        when(deliveryService.getOrdersToBeDelivered()).thenReturn(Collections.singletonList(delivery));
        when(ticketingRule.isTicketRequired(delivery)).thenReturn(true);
        when(ticketService.getTicketByDeliveryId(delivery.getId())).thenReturn(Optional.empty());
        when(ticketingRule.assignPriority()).thenReturn(2);

        ticketScheduler.monitorDeliveries();

        verify(deliveryService, times(1)).getOrdersToBeDelivered();
        verify(ticketingRule, times(1)).isTicketRequired(delivery);
        verify(ticketService, times(1)).getTicketByDeliveryId(delivery.getId());
        verify(ticketService, times(1)).persistTicket(any(Ticket.class));
        verify(deliveryService, times(1)).persistDelivery(delivery);
        assertEquals("TRUE", delivery.getIsTicketCreated());
    }


}