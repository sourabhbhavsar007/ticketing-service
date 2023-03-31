package com.deliveryapp.ticketingservice.service.delivery;

import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.repository.delivery.DeliveryRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);
    private DeliveryRepository deliveryRepository;

    public List<Delivery> getAllDeliveries() {
        logger.info("Fetching all the deliveries...");
        return deliveryRepository.findAll();
    }
}

