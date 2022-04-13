package com.logic.bookstore.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private final String message;
    private final HttpStatus statusCode;
    private final long timestamp;

    protected ServiceException(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = System.currentTimeMillis();
    }
}
