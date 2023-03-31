package com.deliveryapp.ticketingservice.model.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class AuthResponse implements Serializable {

    private final String jwt;

}
