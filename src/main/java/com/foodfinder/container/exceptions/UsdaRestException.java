package com.foodfinder.container.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.SERVICE_UNAVAILABLE, reason="External server error")
public class UsdaRestException extends RuntimeException {

    public UsdaRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
