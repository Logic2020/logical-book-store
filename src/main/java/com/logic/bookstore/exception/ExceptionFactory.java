package com.logic.bookstore.exception;

import org.springframework.http.HttpStatus;

public class ExceptionFactory {

    public static final String BOOK_NOT_FOUND = "Book not found. Book ID: %s";
    public static final String ORDER_NOT_FOUND = "Order not found. Order ID: %s";
    public static final String INVENTORY_NOT_FOUND = "Inventory not found for book. Book ID: %s";
    public static final String BOOK_ALREADY_EXISTS = "Book already exists. Book ID: %s";
    public static final String INVENTORY_ITEM_ALREADY_EXISTS = "Inventory item already exists. Book ID: %s";
    public static final String NOT_ENOUGH_INVENTORY = "Not enough books in inventory. Book ID: %s";

    private ExceptionFactory() {
        // disabling instantiation
    }

    public static ServiceException bookNotFound(String bookId) {
        return new ServiceException(formatMessage(BOOK_NOT_FOUND, bookId), HttpStatus.NOT_FOUND);
    }

    public static ServiceException orderNotFound(String orderId) {
        return new ServiceException(formatMessage(ORDER_NOT_FOUND, orderId), HttpStatus.NOT_FOUND);
    }

    public static ServiceException inventoryNotFound(String orderId) {
        return new ServiceException(formatMessage(INVENTORY_NOT_FOUND, orderId), HttpStatus.NOT_FOUND);
    }

    public static ServiceException bookAlreadyExists(String bookId) {
        return new ServiceException(formatMessage(BOOK_ALREADY_EXISTS, bookId), HttpStatus.BAD_REQUEST);
    }

    public static ServiceException inventoryAlreadyExists(String bookId) {
        return new ServiceException(formatMessage(INVENTORY_ITEM_ALREADY_EXISTS, bookId), HttpStatus.BAD_REQUEST);
    }

    public static ServiceException notEnoughBooks(String bookId) {
        return new ServiceException(formatMessage(NOT_ENOUGH_INVENTORY, bookId), HttpStatus.BAD_REQUEST);
    }

    private static String formatMessage(String message, String id) {
        return String.format(message, id);
    }
}
