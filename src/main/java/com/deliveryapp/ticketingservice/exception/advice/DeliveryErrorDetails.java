package com.deliveryapp.ticketingservice.exception.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class DeliveryErrorDetails {

    private Date timestamp;

    private String message;

    private String details;

}
