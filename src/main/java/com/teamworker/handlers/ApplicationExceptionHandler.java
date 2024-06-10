package com.teamworker.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "Invalid username or password")
    @ExceptionHandler(UsernameNotFoundException.class)
    public void handleException(UsernameNotFoundException e) {
    }
}
