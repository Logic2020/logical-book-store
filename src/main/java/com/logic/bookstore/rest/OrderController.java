package com.logic.bookstore.rest;

import com.logic.bookstore.domain.Order;
import com.logic.bookstore.rest.request.CreateOrderRequest;
import com.logic.bookstore.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public Order placeOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.placeOrder(request.orderedItems());
        LOG.info("New order placed: {}", order);
        return order;
    }

    @GetMapping("/{orderId}")
    public Order getOrderDetails(@PathVariable String orderId) {
        Order order = orderService.findOrder(orderId);
        LOG.info("Retrieved order by id: {}", order);
        return order;
    }

    @GetMapping()
    public Map<String, Order.Status> allOrderStatuses() {
        LOG.info("Returning all order statuses");
        return orderService.allOrderStatuses();
    }
}
