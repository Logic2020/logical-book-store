package com.logic.bookstore.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {

    private Book book;
    private int quantity;

    public OrderItem(Book book, Integer quantity) {
        this.book = book;
        this.quantity = quantity;
    }
}
