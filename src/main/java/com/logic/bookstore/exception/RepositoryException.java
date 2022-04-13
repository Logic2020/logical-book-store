package com.logic.bookstore.exception;

import lombok.Getter;

@Getter
public class RepositoryException extends RuntimeException {

    private final String message;
    private final int code;
    private final long timestamp;

    public static final int NETWORK_ERROR = 0;
    public static final int DATABASE_ERROR = 1;

    public RepositoryException(String message, int code) {
        this.message = message;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }
}
