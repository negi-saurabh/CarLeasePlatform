package com.sogeti.carlease.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ErrorMessageUtility {
    private String errorMessage;
    private String requestingURI;
    private Instant timeStamp;

    public ErrorMessageUtility(String messageError, String URI, Instant timeStamp) {
        this.errorMessage = messageError;
        this.requestingURI = URI;
        this.timeStamp = timeStamp;
    }
}
