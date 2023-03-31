package com.deliveryapp.ticketingservice.rule;

import com.deliveryapp.ticketingservice.model.delivery.Delivery;

public interface TicketingRule {

    boolean isTicketRequired(Delivery delivery);

    int assignPriority();
}
