package com.logic.bookstore.exception;

import org.springframework.http.HttpStatus;

public class RepositoryExceptionFactory {

    public static final String NETWORK_ERROR = "Network error";

    private RepositoryExceptionFactory() {
        // disabling instantiation
    }

    public static RepositoryException networkError() {
        return new RepositoryException(NETWORK_ERROR, RepositoryException.NETWORK_ERROR);
    }
}
