package com.deliveryapp.ticketingservice.scheduler;

import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.TicketStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.repository.delivery.DeliveryRepository;
import com.deliveryapp.ticketingservice.repository.ticket.TicketRepository;
import com.deliveryapp.ticketingservice.rule.EstimatedTimeGreaterThanExpectedTimeTicketingRule;
import com.deliveryapp.ticketingservice.rule.ExpectedDeliveryTimeExceededTicketingRule;
import com.deliveryapp.ticketingservice.rule.TicketingRule;
import io.jsonwebtoken.lang.Collections;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@EnableScheduling
public class TicketScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TicketScheduler.class);

    private DeliveryRepository deliveryRepository;

    private TicketRepository ticketRepository;

    private final Set<TicketingRule> ticketingRules;

    @Autowired
    public TicketScheduler(DeliveryRepository deliveryRepository, TicketRepository ticketRepository, Set<TicketingRule> ticketingRules) {
        this.deliveryRepository = deliveryRepository;
        this.ticketRepository = ticketRepository;
        this.ticketingRules = ticketingRules;
    }

    @PostConstruct
    private void addRules() {
        ticketingRules.add(new ExpectedDeliveryTimeExceededTicketingRule());
        ticketingRules.add(new EstimatedTimeGreaterThanExpectedTimeTicketingRule());
    }

    //Scheduler runs every 5 seconds
    @Scheduled(fixedRate = 5000)//(fixedDelay = 1 * 60 * 1000, initialDelay = 1 * 60 * 1000)
    public void monitorDeliveries() {

        logger.info("Monitoring the deliveries...");
        List<Delivery> ordersToBeDelivered = deliveryRepository.findOrdersToBeDelivered();

        if(!Collections.isEmpty(ordersToBeDelivered)) {

            for (Delivery delivery : ordersToBeDelivered) {

                logger.info(delivery.toString());
                Optional<TicketingRule> ticketingRule = ticketingRules.stream()
                        .filter(rule -> rule.isTicketRequired(delivery))
                        .findFirst();

                if(ticketingRule.isPresent() && !isExistingTicketPresent(delivery.getId())) {
                    createTicket(delivery, ticketingRule.get().assignPriority());
                }
            }
        }
    }

    private boolean isExistingTicketPresent(Long deliveryId) {

        Optional<Ticket> ticket = ticketRepository.findByDeliveryId(deliveryId);

        if(ticket.isPresent()){
            logger.info("Ticket already created for the delivery Id : " + deliveryId);
            return true;
        } else {
            logger.info("Ticket needs to be created for the delivery Id : " + deliveryId);
            return false;
        }

    }

    private void createTicket(Delivery delivery, int priority) {

        logger.info("Creating the ticket...");

        //We check if customer is VIP, and assign highest priority 1 to VIP customer
        //If the customer is VIP, the priority of ticket would be 1 irrespective of the satisfying ticketing rule
        priority = isCustomerVIP(delivery.getCustomerType()) ? 1 : priority;

        Ticket ticket = Ticket.builder()
                .deliveryId(delivery.getId())
                .priority(priority)
                .status(TicketStatus.CREATED)
                .build();

        ticketRepository.save(ticket);

        //We also update the delivery attribute isTicketCreated
        //So that we don't fetch it again, and reduce the I/O and size of fetched records and collection object.
        delivery.setIsTicketCreated("TRUE");
        deliveryRepository.save(delivery);

        logger.info("Ticket created : {}", ticket);
    }

    private boolean isCustomerVIP(String customerType) {
        return (Strings.isNotBlank(customerType)) &&(CustomerType.VIP.equalsIgnoreCase(customerType));
    }


}
