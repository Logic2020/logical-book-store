package com.logic.bookstore.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<ErrorResponse> handleApplicationExceptions(ServiceException e) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), e.getStatusCode().value(), e.getMessage());
        return new ResponseEntity<>(response, e.getStatusCode());
    }
}
