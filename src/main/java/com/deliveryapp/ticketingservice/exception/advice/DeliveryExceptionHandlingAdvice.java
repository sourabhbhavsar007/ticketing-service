package com.deliveryapp.ticketingservice.exception.advice;

import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * This class is responsible for handling all un-handled runtime exceptions and log them.
 *
 */
@RestControllerAdvice
public class DeliveryExceptionHandlingAdvice {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryExceptionHandlingAdvice.class);


    /**
     * Handles constraint violation exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<DeliveryErrorDetails> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {

        logger.error(exception.getMessage(), exception);

        DeliveryErrorDetails errorDetails = new DeliveryErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

    /**
     * Handles method argument not valid exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DeliveryErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {

        logger.error(exception.getMessage(), exception);

        DeliveryErrorDetails errorDetails = new DeliveryErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

    /**
     * Handles method argument type mismatch exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<DeliveryErrorDetails> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, WebRequest request) {

        logger.error(exception.getMessage(), exception);

        DeliveryErrorDetails errorDetails = new DeliveryErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

    /**
     * Handles ExpiredJwtException when jwt token is expired
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<DeliveryErrorDetails> handleExpiredJWTException(ExpiredJwtException exception, WebRequest request) {

        logger.error(exception.getMessage(), exception);

        DeliveryErrorDetails errorDetails = new DeliveryErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);

    }

    /**
     * Handles SignatureException when jwt token is invalid
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<DeliveryErrorDetails> handleSignatureException(SignatureException exception, WebRequest request) {

        logger.error(exception.getMessage(), exception);

        DeliveryErrorDetails errorDetails = new DeliveryErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);

    }

    /**
     * Global exception handler, for super Exception class
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DeliveryErrorDetails> globalExceptionHandler(Exception ex, WebRequest request) {

        logger.error(ex.getMessage(), ex);

        DeliveryErrorDetails errorDetails = new DeliveryErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
