package com.logic.bookstore.domain;

import lombok.Data;

@Data
public class InventoryItem {

    private String bookId;
    private int amount;

    public InventoryItem(String bookId, int amount) {
        this.bookId = bookId;
        this.amount = amount;
    }
}
