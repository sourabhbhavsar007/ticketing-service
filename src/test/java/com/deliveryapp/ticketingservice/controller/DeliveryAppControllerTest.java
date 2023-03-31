package com.deliveryapp.ticketingservice.controller;

import com.deliveryapp.ticketingservice.constants.CustomerType;
import com.deliveryapp.ticketingservice.constants.DeliveryStatus;
import com.deliveryapp.ticketingservice.constants.TicketStatus;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.model.user.User;
import com.deliveryapp.ticketingservice.service.delivery.DeliveryService;
import com.deliveryapp.ticketingservice.service.ticket.TicketService;
import com.deliveryapp.ticketingservice.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DeliveryAppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryService deliveryService;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private UserService userService;

    private List<Delivery> deliveryList;

    private List<Ticket> ticketList;

    private List<User> userList;


    @BeforeEach
    void setUp() {

        //populating deliveryList
        deliveryList = new ArrayList<>();

        deliveryList.add(Delivery.builder()
                .customerType(CustomerType.VIP)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(2.4f)
                .expectedDeliveryTime(LocalDateTime.now().plusMinutes(2))
                .riderRating(7.5f)
                .meanPreparationTime(35)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(25))
                .isTicketCreated("FALSE")
                .build());

        deliveryList.add(Delivery.builder()
                .customerType(CustomerType.VIP)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(4.5f)
                .expectedDeliveryTime(LocalDateTime.now().plusMinutes(20))
                .riderRating(8.6f)
                .meanPreparationTime(25)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(40))
                .isTicketCreated("FALSE")
                .build());

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

        deliveryList.add(Delivery.builder()
                .customerType(CustomerType.NEW)
                .status(DeliveryStatus.PREPARING)
                .currentDistanceFromDestination(2.4f)
                .expectedDeliveryTime(LocalDateTime.now().plusMinutes(2))
                .riderRating(7.5f)
                .meanPreparationTime(35)
                .timeToReachDestination(LocalDateTime.now().plusMinutes(25))
                .isTicketCreated("FALSE")
                .build());

        //populating ticketList
        ticketList = new ArrayList<>();

        ticketList.add(Ticket.builder()
                .priority(1)
                .status(TicketStatus.CREATED)
                .deliveryId(3l)
                .build());

        ticketList.add(Ticket.builder()
                .priority(1)
                .status(TicketStatus.CREATED)
                .deliveryId(4l)
                .build());

        ticketList.add(Ticket.builder()
                .priority(2)
                .status(TicketStatus.CREATED)
                .deliveryId(5l)
                .build());

        ticketList.add(Ticket.builder()
                .priority(3)
                .status(TicketStatus.CREATED)
                .deliveryId(6l)
                .build());

        //populating userList
        userList = new ArrayList<>();

        userList.add(User.builder()
                .username("user1")
                .password("user1")
                .build());

        userList.add(User.builder()
                .username("user2")
                .password("user2")
                .build());
    }


    @AfterEach
    void tearDown() {
        deliveryList.removeAll(deliveryList);
        ticketList.removeAll(ticketList);
        userList.removeAll(userList);
    }


    @Test
    @WithMockUser(username = "user1", password = "user1")
    void testGetAllDeliveries() throws Exception {

        when(deliveryService.getAllDeliveries()).thenReturn(deliveryList);

        mockMvc.perform(get("/api/v1/deliveries").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerType", is(CustomerType.VIP)));
    }

    @Test
    @WithMockUser(username = "user1", password = "user1")
    void testGetAllUsers() throws Exception {

        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("user1")));
    }

    @Test
    @WithMockUser(username = "user1", password = "user1")
    void testGetAllTickets() throws Exception {

        when(ticketService.getAllTickets()).thenReturn(ticketList);

        mockMvc.perform(get("/api/v1/tickets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is(TicketStatus.CREATED)));
    }

    @Test
    @WithMockUser(username = "user1", password = "user1")
    void testGetTicketReturnsHighestPriorityForVIPCustomers() throws Exception {

        when(ticketService.getAllTickets()).thenReturn(ticketList);

        mockMvc.perform(get("/api/v1/tickets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority", is(1)))
                .andExpect(jsonPath("$[0].deliveryId", is(3)));
    }

    @Test
    @WithMockUser(username = "user1", password = "user1")
    void testGetTicketReturnsSecondPriorityIfDeliveryTimeExceeded() throws Exception {

        when(ticketService.getAllTickets()).thenReturn(ticketList);

        mockMvc.perform(get("/api/v1/tickets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2].priority", is(2)))
                .andExpect(jsonPath("$[2].deliveryId", is(5)));
    }


}
