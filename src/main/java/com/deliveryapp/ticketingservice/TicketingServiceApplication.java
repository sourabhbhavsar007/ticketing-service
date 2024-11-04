package com.deliveryapp.ticketingservice;

import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.DeliveryStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.model.user.User;
import com.deliveryapp.ticketingservice.repository.UserRepository;
import com.deliveryapp.ticketingservice.repository.delivery.DeliveryRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.time.LocalDateTime;

//@EnableEurekaClient
@EnableCaching
@SpringBootApplication(exclude = {org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration.class})
@AllArgsConstructor
public class TicketingServiceApplication implements CommandLineRunner {

    private DeliveryRepository deliveryRepository;
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(TicketingServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //creating some preloaded users
        userRepository.save(User.builder()
                .username("user1")
                .password("user1")
                .build());

        userRepository.save(User.builder()
                .username("user2")
                .password("user2")
                .build());

        //Creating some preloaded delivery items
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
}
