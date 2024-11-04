package com.deliveryapp.ticketingservice.service.delivery;

import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.DeliveryStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.repository.delivery.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery delivery1;
    private Delivery delivery2;

    List<Delivery> deliveryList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        deliveryService = new DeliveryService(deliveryRepository, cacheManager);

        delivery1 = Delivery.builder()
                .customerType(CustomerType.LOYAL)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(3.4f)
                .expectedDeliveryTime(LocalDateTime.now().minusMinutes(10))
                .riderRating(9.3f)
                .meanPreparationTime(35)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(15))
                .isTicketCreated("FALSE")
                .build();

        delivery2 = Delivery.builder()
                .customerType(CustomerType.VIP)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(4.5f)
                .expectedDeliveryTime(LocalDateTime.now().minusMinutes(10))
                .riderRating(7.3f)
                .meanPreparationTime(25)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(20))
                .isTicketCreated("FALSE")
                .build();

        Collections.addAll(deliveryList, delivery1, delivery2);

    }

    @Test
    void testGetAllDeliveries_thenAllDeliveriesFetched() {
        when(deliveryRepository.findAll()).thenReturn(deliveryList);
        List<Delivery> fetchedDeliveries = deliveryService.getAllDeliveries();
        assertThat(fetchedDeliveries.size()).isGreaterThan(0);
    }


    @Test
    public void testGetAllDeliveries() {
        when(deliveryRepository.findAll()).thenReturn(Arrays.asList(delivery1, delivery2));
        List<Delivery> deliveries = deliveryService.getAllDeliveries();

        assertNotNull(deliveries);
        assertEquals(2, deliveries.size());
        verify(deliveryRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrdersToBeDelivered() {

        when(deliveryRepository.findOrdersToBeDelivered()).thenReturn(Collections.singletonList(delivery1));

        List<Delivery> ordersToBeDelivered = deliveryService.getOrdersToBeDelivered();

        assertNotNull(ordersToBeDelivered);
        assertEquals(1, ordersToBeDelivered.size());
        assertEquals(DeliveryStatus.PREPARING, ordersToBeDelivered.get(0).getStatus());
        verify(deliveryRepository, times(1)).findOrdersToBeDelivered();
    }

    @Test
    public void testPersistDelivery() {
        deliveryService.persistDelivery(delivery1);
        verify(deliveryRepository, times(1)).save(delivery1);
    }

    @Test
    public void testClearDeliveryCache() {
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("deliveries")).thenReturn(cache);

        deliveryService.clearDeliveryCache(delivery1.getId());
        verify(cache, times(1)).evict(delivery1.getId());
    }

    @Test
    public void testFallbackTicketResponse() {
        String response = deliveryService.fallbackTicketResponse(new RuntimeException("Failure caused"));
        assertEquals("Something went wrong, please try again later.", response);
    }

}
