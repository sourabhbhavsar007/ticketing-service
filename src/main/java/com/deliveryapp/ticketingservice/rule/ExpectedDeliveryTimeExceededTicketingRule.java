package com.deliveryapp.ticketingservice.rule;

import com.deliveryapp.ticketingservice.model.delivery.Delivery;

import java.time.LocalDateTime;

public class ExpectedDeliveryTimeExceededTicketingRule implements  TicketingRule {

    @Override
    public boolean isTicketRequired(Delivery delivery) {
        return (null != delivery.getExpectedDeliveryTime()) && (LocalDateTime.now().isAfter(delivery.getExpectedDeliveryTime()));
    }

    //We assign the second highest priority for this condition.
    @Override
    public int assignPriority() {
        return 2;
    }
}
