package com.deliveryapp.ticketingservice.service.user;

import com.deliveryapp.ticketingservice.model.user.User;
import com.deliveryapp.ticketingservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        logger.info("Fetching all the users...");
        return userRepository.findAll();
    }
}
