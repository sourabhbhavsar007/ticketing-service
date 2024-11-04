package com.deliveryapp.ticketingservice.repository.delivery;

import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Cacheable("orders")
    @Query("SELECT d FROM Delivery d WHERE d.status <> 'DELIVERED' AND d.isTicketCreated <> 'TRUE'")
    List<Delivery> findOrdersToBeDelivered();

}
