package com.deliveryapp.ticketingservice.service.user;

import com.deliveryapp.ticketingservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserAuthDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<com.deliveryapp.ticketingservice.model.user.User> searchedUser = userRepository.findByUsername(username);

        searchedUser.orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

        return new User(searchedUser.get().getUsername(), searchedUser.get().getPassword(), new ArrayList<>());

    }
}
