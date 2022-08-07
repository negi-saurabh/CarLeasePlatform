package com.sogeti.carlease.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class CarNotFoundException extends HttpStatusCodeException {

    private static final long serialVersionUID = 73263616501570402L;

    public CarNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public CarNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
