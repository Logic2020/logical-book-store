package com.logic.bookstore.rest;

import com.logic.bookstore.domain.Order;
import com.logic.bookstore.rest.request.OrderRequest;
import com.logic.bookstore.service.OrderService;
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

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public Order placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(request.orderedItems());
    }

    @GetMapping("/{orderId}")
    public Order getOrderDetails(@PathVariable String orderId) {
        return orderService.findOrder(orderId);
    }

    @GetMapping()
    public Map<String, Order.Status> allOrderStatuses() {
        return orderService.allOrderStatuses();
    }
}
