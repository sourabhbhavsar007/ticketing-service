package com.deliveryapp.ticketingservice.model.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AuthRequest implements Serializable {

    private String username;
    private String password;
}
