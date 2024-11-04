package com.deliveryapp.ticketingservice.service.delivery;

import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.repository.delivery.DeliveryRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);
    private DeliveryRepository deliveryRepository;
    private CacheManager cacheManager;

    @CircuitBreaker(name = "deliveryCircuitBreaker", fallbackMethod = "fallbackTicketResponse")
    @Cacheable("deliveries")
    public List<Delivery> getAllDeliveries() {
        logger.info("Fetching all the deliveries...");
        return deliveryRepository.findAll();
    }

    @CircuitBreaker(name = "orderCircuitBreaker", fallbackMethod = "fallbackTicketResponse")
    @Cacheable("orders")
    public List<Delivery> getOrdersToBeDelivered() {
        logger.info("Fetching all the orders to be delivered...");
        return deliveryRepository.findOrdersToBeDelivered();
    }

    @CacheEvict(value = "deliveries", allEntries = true)
    public void persistDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    //Normally we will call cache evict on update or delete operations
    public void clearDeliveryCache(Long ticketId) {
        Objects.requireNonNull(cacheManager.getCache("deliveries")).evict(ticketId);
    }

    public String fallbackTicketResponse(Throwable t) {
        return "Something went wrong, please try again later.";
    }

}

