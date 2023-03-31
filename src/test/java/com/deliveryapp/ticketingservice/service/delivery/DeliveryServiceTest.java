package com.deliveryapp.ticketingservice.service.delivery;

import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.DeliveryStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.repository.delivery.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    DeliveryService deliveryService;

    List<Delivery> deliveryList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        deliveryService = new DeliveryService(deliveryRepository);

        deliveryList.add(Delivery.builder()
                .customerType(CustomerType.LOYAL)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(3.4f)
                .expectedDeliveryTime(LocalDateTime.now().minusMinutes(10))
                .riderRating(9.3f)
                .meanPreparationTime(35)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(15))
                .isTicketCreated("FALSE")
                .build());

    }


    @Test
    void testGetAllDeliveries_thenAllDeliveriesFetched() {

        when(deliveryRepository.findAll()).thenReturn(deliveryList);

        List<Delivery> fetchedDeliveries = deliveryService.getAllDeliveries();

        assertThat(fetchedDeliveries.size()).isGreaterThan(0);
    }

}
