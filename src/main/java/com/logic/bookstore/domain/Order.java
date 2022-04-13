package com.logic.bookstore.domain;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Order {

    public enum Status {
        IN_PROGRESS, SUCCESS, FAIL
    }

    private String orderId;

    private Status status;

    private List<OrderItem> items;

    private long timestamp;

    public Order(String orderId, List<OrderItem> items) {
        this.orderId = orderId;
        this.status = Status.IN_PROGRESS;
        this.items = items;
        this.timestamp = Instant.now().toEpochMilli();
    }
}
