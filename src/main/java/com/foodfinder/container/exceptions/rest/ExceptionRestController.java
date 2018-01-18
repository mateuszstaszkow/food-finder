package com.foodfinder.container.exceptions.rest;

import com.foodfinder.container.exceptions.StashingException;
import com.foodfinder.container.exceptions.UsdaRestException;
import com.foodfinder.container.exceptions.service.ExceptionStasher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExceptionRestController {

    private final ExceptionStasher exceptionStasher;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundException(NotFoundException exception) {
        exceptionStasher.stash(exception, "");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public void handleBadRequestException(BadRequestException exception) {
        exceptionStasher.stash(exception, "");
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(UsdaRestException.class)
    public void handleUsdaRestException(UsdaRestException exception) {
        exceptionStasher.stash(exception, "");
    }

    @ExceptionHandler(StashingException.class)
    public void handleStashingException(StashingException exception) {
        exceptionStasher.stash(exception, "");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedException.class)
    public void handleNotAuthorizedException(NotAuthorizedException exception) {
        exceptionStasher.stash(exception, "");
    }
}
