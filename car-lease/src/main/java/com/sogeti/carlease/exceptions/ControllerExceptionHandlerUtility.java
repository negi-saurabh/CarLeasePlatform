package com.sogeti.carlease.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandlerUtility {

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        Instant timeStamp = Instant.now();
        ErrorMessageUtility errorMessage = new ErrorMessageUtility(ex.getLocalizedMessage(), request.getDescription(true),
                timeStamp);
        return new ResponseEntity<Object>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({CarNotFoundException.class})
    public final ResponseEntity<Object> handleCarException(CarNotFoundException ex, WebRequest request) {
        Instant timeStamp = Instant.now();
        ErrorMessageUtility errorMessage = new ErrorMessageUtility(ex.getMessage(), request.getDescription(false),
                timeStamp);
        return new ResponseEntity<Object>(errorMessage, ex.getStatusCode());
    }

    @ExceptionHandler({CustomerNotFoundException.class})
    public final ResponseEntity<Object> handleCustomerException(CustomerNotFoundException ex, WebRequest request) {
        Instant timeStamp = Instant.now();
        ErrorMessageUtility errorMessage = new ErrorMessageUtility(ex.getMessage(), request.getDescription(false),
                timeStamp);
        return new ResponseEntity<Object>(errorMessage, ex.getStatusCode());
    }
}
