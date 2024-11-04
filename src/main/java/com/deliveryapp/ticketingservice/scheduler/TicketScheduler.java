package com.deliveryapp.ticketingservice.scheduler;

import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.TicketStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.rule.EstimatedTimeGreaterThanExpectedTimeTicketingRule;
import com.deliveryapp.ticketingservice.rule.ExpectedDeliveryTimeExceededTicketingRule;
import com.deliveryapp.ticketingservice.rule.TicketingRule;
import com.deliveryapp.ticketingservice.service.delivery.DeliveryService;
import com.deliveryapp.ticketingservice.service.ticket.TicketService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@EnableScheduling
public class TicketScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TicketScheduler.class);

    private final DeliveryService deliveryService;

    private final TicketService ticketService;

    private final Set<TicketingRule> ticketingRules;

    @Autowired
    public TicketScheduler(DeliveryService deliveryService, TicketService ticketService, Set<TicketingRule> ticketingRules) {
       this.deliveryService = deliveryService;
        this.ticketService = ticketService;
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

        //As compared to directly calling persistence layer earlier, using a service layer in between which is having @Cacheable for efficiency.
        List<Delivery> ordersToBeDelivered = deliveryService.getOrdersToBeDelivered();

        if(!ordersToBeDelivered.isEmpty()) { //check if there are any orders for monitoring

            for (Delivery delivery : ordersToBeDelivered) {

                logger.info(delivery.toString());

                //Introducing MDC for structured logging and tracking
                MDC.put("correlationId", UUID.randomUUID().toString());
                logger.info("Applying rules with correlation ID : {}", MDC.get("correlationId"));

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

        //As compared to directly calling persistence layer earlier, using a service layer in between which is having @Cacheable for efficiency.
        Optional<Ticket> ticket = ticketService.getTicketByDeliveryId(deliveryId);

        if(ticket.isPresent()){
            logger.info("Ticket already created for the delivery Id : {}", deliveryId);
            return true;
        } else {
            logger.info("Ticket needs to be created for the delivery Id : {}", deliveryId);
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

        ticketService.persistTicket(ticket);

        //We also update the delivery attribute isTicketCreated
        //So that we don't fetch it again, and reduce the I/O and size of fetched records and collection object.
        delivery.setIsTicketCreated("TRUE");
        deliveryService.persistDelivery(delivery);

        logger.info("Ticket created : {}", ticket);
    }

    private boolean isCustomerVIP(String customerType) {
        return (Strings.isNotBlank(customerType)) &&(CustomerType.VIP.equalsIgnoreCase(customerType));
    }


}
