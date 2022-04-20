package com.logic.bookstore.service;

import com.logic.bookstore.domain.Book;
import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.domain.Order;
import com.logic.bookstore.domain.OrderItem;
import com.logic.bookstore.exception.ExceptionFactory;
import com.logic.bookstore.exception.ServiceException;
import com.logic.bookstore.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.logic.bookstore.TestDataBuilder.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    // under test
    private OrderService orderService;

    private OrderRepository orderRepository;
    private ProductCatalogService catalog;
    private InventoryService inventory;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        catalog = mock(ProductCatalogService.class);
        inventory = mock(InventoryService.class);
        orderService = new OrderService(orderRepository, catalog, inventory);

        orderCaptor = ArgumentCaptor.forClass(Order.class);
    }

    @Test
    void allOrderStatusesReturnsEmptyMapIfNoOrdersPresent() {
        // given
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        Map<String, Order.Status> result = orderService.allOrderStatuses();
        // then
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void allOrderStatusesReturnStatusesForEachExistingOrder() {
        // given
        Order firstOrder = order().orderId("1000").status(Order.Status.IN_PROGRESS).build();
        Order secondOrder = order().orderId("2000").status(Order.Status.SUCCESS).build();
        when(orderRepository.findAll()).thenReturn(List.of(firstOrder, secondOrder));
        // when
        Map<String, Order.Status> result = orderService.allOrderStatuses();
        // then
        assertThat(result.get("1000"), is(Order.Status.IN_PROGRESS));
        assertThat(result.get("2000"), is(Order.Status.SUCCESS));
    }

    @Test
    void findOrderReturnsOrderIfExists() {
        // given
        Order order = order().orderId("1000").build();
        when(orderRepository.findById("1000")).thenReturn(Optional.of(order));
        // when
        Order result = orderService.findOrder("1000");
        // then
        assertThat(result, is(order));
    }

    @Test
    void placeOrderCreatesNewOrderIfEnoughInventory() {
        // given
        String firstBookId = DATA_INTENSIVE_APPLICATIONS.getId();
        Map<String, Integer> requestedBookQuantities = Map.of(firstBookId, 5);
        when(inventory.getInventoryFor(firstBookId)).thenReturn(new InventoryItem(firstBookId, 5));
        when(catalog.getBook(firstBookId)).thenReturn(DATA_INTENSIVE_APPLICATIONS);
        Order expectedOrder = Order.builder()
                .status(Order.Status.IN_PROGRESS)
                .items(List.of(new OrderItem(DATA_INTENSIVE_APPLICATIONS, 5),
                               new OrderItem(REFACTORING_TO_PATTERNS, 3)))
                .build();
        when(orderRepository.save(expectedOrder)).thenReturn(expectedOrder);
        // when
        Order result = orderService.placeOrder(requestedBookQuantities);
        // then
        verify(inventory, times(1)).ensureEnoughInventory(requestedBookQuantities);
        verify(inventory, times(1)).decreaseAmount(firstBookId, 5);
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        Order actualOrder = orderCaptor.getValue();
        assertThat(result.getOrderId(), is(actualOrder.getOrderId()));
        assertThat(result.getStatus(), is(Order.Status.IN_PROGRESS));
        assertThat(result.getItems(), is(actualOrder.getItems()));
    }

    @Test
    void attemptToGetNonExistentOrderThrowsException() {
        // given
        when(orderRepository.findById("1000")).thenReturn(Optional.empty());
        // when
        // then
        ServiceException exception = assertThrowsExactly(ServiceException.class,
                                                         () -> orderService.findOrder("1000"));
        assertThat(exception.getMessage(), is("Order not found. Order ID: " + 1000));
        assertThat(exception.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void placeOrderThrowsIfNotEnoughInventory() {
        // given
        Book book = DATA_INTENSIVE_APPLICATIONS;
        Map<String, Integer> requestedBookQuantities = Map.of(book.getId(), 2);
        doThrow(ExceptionFactory.notEnoughBooks(book.getId())).when(inventory).ensureEnoughInventory(requestedBookQuantities);
        // when
        // then
        ServiceException exception = assertThrowsExactly(ServiceException.class,
                                                         () -> orderService.placeOrder(requestedBookQuantities));
        assertThat(exception.getMessage(), is("Not enough books in inventory. Book ID: " + book.getId()));
        assertThat(exception.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}
