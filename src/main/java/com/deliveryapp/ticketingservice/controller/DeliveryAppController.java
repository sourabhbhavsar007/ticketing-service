package com.deliveryapp.ticketingservice.controller;

import com.deliveryapp.ticketingservice.model.auth.AuthRequest;
import com.deliveryapp.ticketingservice.model.auth.AuthResponse;
import com.deliveryapp.ticketingservice.model.delivery.Delivery;
import com.deliveryapp.ticketingservice.model.ticket.Ticket;
import com.deliveryapp.ticketingservice.model.user.User;
import com.deliveryapp.ticketingservice.service.delivery.DeliveryService;
import com.deliveryapp.ticketingservice.service.ticket.TicketService;
import com.deliveryapp.ticketingservice.service.user.UserAuthDetailsService;
import com.deliveryapp.ticketingservice.service.user.UserService;
import com.deliveryapp.ticketingservice.util.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1")
public class DeliveryAppController {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryAppController.class);
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private UserAuthDetailsService userAuthDetailsService;
    private DeliveryService deliveryService;
    private TicketService ticketService;
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        logger.info("Auth request received by user : {}", authRequest.getUsername());
        return getAuthResponse(authRequest);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getTickets(){
        logger.info("Request to fetch the tickets...");
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
    }

    @GetMapping("/deliveries")
    public ResponseEntity<List<Delivery>> getDeliveries(){
        logger.info("Request to fetch the deliveries...");
        return new ResponseEntity<>(deliveryService.getAllDeliveries(), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        logger.info("Request to fetch the deliveries...");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    private ResponseEntity<AuthResponse> getAuthResponse(AuthRequest authRequest) throws Exception {

        try {
            logger.info("Authenticating the user...");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        }
        catch (BadCredentialsException badCredentialsException) {
            logger.error("Invalid username or password : {}", badCredentialsException.getMessage());
            throw new Exception("Invalid username or password", badCredentialsException);
        }

        final UserDetails userDetails = userAuthDetailsService.loadUserByUsername(authRequest.getUsername());

        final String jwt = jwtUtils.generateToken(userDetails);

        logger.info("User authenticated successfully, sending jwt token...");
        return ok(new AuthResponse(jwt));
    }

}
