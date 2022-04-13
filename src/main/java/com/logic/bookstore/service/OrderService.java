package com.logic.bookstore.service;

import com.logic.bookstore.domain.Order;
import com.logic.bookstore.domain.OrderItem;
import com.logic.bookstore.exception.ExceptionFactory;
import com.logic.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductCatalogService catalog;
    private final InventoryService inventory;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ProductCatalogService catalog,
                        InventoryService inventory) {
        this.orderRepository = orderRepository;
        this.catalog = catalog;
        this.inventory = inventory;
    }

    public Order placeOrder(Map<String, Integer> requestedQuantities) {
        List<OrderItem> orderItems = findAllItems(requestedQuantities);
        removeBoughtItemsFromInventory(orderItems);

        return createAndSaveOrder(orderItems);
    }

    public Order findOrder(String id) {
            return orderRepository.findById(id)
            .orElseThrow(() -> ExceptionFactory.orderNotFound(id));
    }

    public Map<String, Order.Status> allOrderStatuses() {
        return orderRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Order::getOrderId, Order::getStatus));
    }

    private List<OrderItem> findAllItems(Map<String, Integer> requestedQuantities) {
        return requestedQuantities.entrySet().stream()
                .map(entry -> new OrderItem(catalog.getBook(entry.getKey()), entry.getValue()))
                .collect(Collectors.toList());
    }

    private void removeBoughtItemsFromInventory(List<OrderItem> orderItems) {
        orderItems.forEach(item -> inventory.decreaseAmount(item.getBook().getId(), item.getQuantity()));
    }

    private Order createAndSaveOrder(List<OrderItem> orderItems) {
        try {
            Order order = new Order(generateOrderId(), orderItems);
            orderRepository.save(order);
            return order;
        } catch (Exception e) {
            throw new RuntimeException("Unknown error");
        }
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }
}
