package com.foodfinder.container.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

@ControllerAdvice
public class ExceptionRestController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundException() {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BadRequestException.class)
    public void handleBadRequestException() {}
}
