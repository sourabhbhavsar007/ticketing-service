package com.deliveryapp.ticketingservice.repository.delivery;

import com.deliveryapp.ticketingservice.TicketingServiceApplication;
import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.DeliveryStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeliveryRepositoryTest {
   @Autowired
   DeliveryRepository deliveryRepository;

    @BeforeEach
    void setUp(){
        deliveryRepository.save(Delivery.builder()
                .customerType(CustomerType.VIP)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(2.4f)
                .expectedDeliveryTime(LocalDateTime.now().plusMinutes(2))
                .riderRating(7.5f)
                .meanPreparationTime(35)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(25))
                .isTicketCreated("FALSE")
                .build());

        deliveryRepository.save(Delivery.builder()
                .customerType(CustomerType.VIP)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(4.5f)
                .expectedDeliveryTime(LocalDateTime.now().plusMinutes(20))
                .riderRating(8.6f)
                .meanPreparationTime(25)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(40))
                .isTicketCreated("FALSE")
                .build());

        deliveryRepository.save(Delivery.builder()
                .customerType(CustomerType.LOYAL)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(3.4f)
                .expectedDeliveryTime(LocalDateTime.now().minusMinutes(10))
                .riderRating(9.3f)
                .meanPreparationTime(35)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(15))
                .isTicketCreated("FALSE")
                .build());

        deliveryRepository.save(Delivery.builder()
                .customerType(CustomerType.NEW)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(2.4f)
                .expectedDeliveryTime(LocalDateTime.now().plusMinutes(2))
                .riderRating(7.5f)
                .meanPreparationTime(35)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(25))
                .isTicketCreated("FALSE")
                .build());
    }

    @AfterEach
    void tearDown(){
        deliveryRepository.deleteAll();
    }


    @Test
    void testGivenList_whenGettingAllDeliveries_thenGetAllDeliveries() {

        List<Delivery> resultList = deliveryRepository.findAll();

        assertThat(resultList.size()).isGreaterThan(0);
    }

    @Test
    public void testFindById_NonExistentId() {
        Optional<Delivery> result = deliveryRepository.findById(999L);
        assertFalse(result.isPresent());
    }

}
