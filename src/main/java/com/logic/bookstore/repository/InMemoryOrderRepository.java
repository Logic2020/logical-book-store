package com.logic.bookstore.repository;

import com.logic.bookstore.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> orders;

    public InMemoryOrderRepository() {
        orders = new HashMap<>();
    }

    @Override
    public Order save(Order order) {
        return orders.put(order.getOrderId(), order);
    }

    @Override
    public Optional<Order> findById(String id) {
        if (!orders.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }
}
