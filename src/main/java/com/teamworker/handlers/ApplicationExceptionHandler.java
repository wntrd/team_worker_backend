package com.teamworker.handlers;

import com.teamworker.security.jwt.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "Неправильний логін або пароль")
    @ExceptionHandler(UsernameNotFoundException.class)
    public void handleException(UsernameNotFoundException e) {
    }

    @ResponseStatus(
            value = HttpStatus.UNAUTHORIZED,
            reason = "Час дії токену вичерпано або він є недійсним")
    @ExceptionHandler(JwtAuthenticationException.class)
    public void handleTokenException(JwtAuthenticationException e) {

    }
}
