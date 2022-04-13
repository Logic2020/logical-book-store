package com.logic.bookstore.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logic.bookstore.TestDataBuilder;
import com.logic.bookstore.domain.Order;
import com.logic.bookstore.exception.CustomExceptionHandler;
import com.logic.bookstore.exception.ExceptionFactory;
import com.logic.bookstore.rest.request.CreateOrderRequest;
import com.logic.bookstore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Autowired
    CustomExceptionHandler customExceptionHandler;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper();
    }

    @Test
    void placeOrderCreatesNewOrder() throws Exception {
        // given
        CreateOrderRequest request = new CreateOrderRequest(List.of(
                new CreateOrderRequest.Item("1000", 2),
                new CreateOrderRequest.Item("2000", 1)));
        // when
        mockMvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        // then
        verify(orderService, times(1)).placeOrder(request.orderedItems());
    }

    @Test
    void getOrderReturnsOrderById() throws Exception {
        // given
        Order expectedOrder = TestDataBuilder.order().build();
        when(orderService.findOrder(expectedOrder.getOrderId())).thenReturn(expectedOrder);
        // when
        MvcResult result = mockMvc.perform(
                get("/orders/" + expectedOrder.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        // then
        Order returnedOrder = mapper.readValue(result.getResponse().getContentAsString(), Order.class);
        assertThat(returnedOrder, is(expectedOrder));
    }

    @Test
    void getOrdersReturnsStatusesOfAllSubmittedOrders() throws Exception {
        // given
        String orderId1 = UUID.randomUUID().toString();
        String orderId2 = UUID.randomUUID().toString();
        Map<String, Order.Status> submittedOrders = Map.of(orderId1, Order.Status.IN_PROGRESS,
                                                           orderId2, Order.Status.SUCCESS);
        when(orderService.allOrderStatuses()).thenReturn(submittedOrders);
        // when
        MvcResult result = mockMvc.perform(
                get("/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        // then

        Map<String, Order.Status> returnedOrders = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Order.Status>>() {
        });
        assertThat(returnedOrders.get(orderId1), is(Order.Status.IN_PROGRESS));
        assertThat(returnedOrders.get(orderId2), is(Order.Status.SUCCESS));
    }

    @Test
    void attemptingToGetNonExistentOrderByIdThrowsException() throws Exception {
        when(orderService.findOrder("123")).thenThrow(ExceptionFactory.orderNotFound("123"));
        // when
        MvcResult result = mockMvc.perform(
                get("/orders/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();
        // then
        assertTrue(result.getResponse().getContentAsString().contains("Order not found"));
    }
}
