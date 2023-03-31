package com.deliveryapp.ticketingservice.rule;

import com.deliveryapp.ticketingservice.constants.DeliveryStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class EstimatedTimeGreaterThanExpectedTimeTicketingRule implements  TicketingRule {

    @Override
    public boolean isTicketRequired(Delivery delivery) {

        long estimatedTime = ChronoUnit.MINUTES.between(LocalDateTime.now(), delivery.getTimeToReachDestination().plusMinutes(delivery.getMeanPreparationTime()));

        long expectedTime = ChronoUnit.MINUTES.between(LocalDateTime.now(), delivery.getExpectedDeliveryTime());

        return (estimatedTime > expectedTime) && (DeliveryStatus.PREPARING.equalsIgnoreCase(delivery.getStatus()));

    }

    @Override
    public int assignPriority() {
        return 3;
    }
}
