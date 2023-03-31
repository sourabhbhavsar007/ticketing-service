package com.deliveryapp.ticketingservice.model.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String customerType;

    @Column
    private String status;

    @Column
    private LocalDateTime expectedDeliveryTime;

    @Column
    private float currentDistanceFromDestination;

    @Column
    private float riderRating;

    @Column
    private int meanPreparationTime;

    @Column
    private LocalDateTime timeToReachDestination;

    @Column
    private String isTicketCreated;

}
