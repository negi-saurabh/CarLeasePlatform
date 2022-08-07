package com.sogeti.carlease.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class CustomerNotFoundException extends HttpStatusCodeException {

    private static final long serialVersionUID = 73263616501570402L;

    public CustomerNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public CustomerNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
