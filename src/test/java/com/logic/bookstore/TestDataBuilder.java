package com.logic.bookstore;

import com.logic.bookstore.domain.Book;
import com.logic.bookstore.domain.Order;
import com.logic.bookstore.domain.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class TestDataBuilder {

    public static final Book DOMAIN_DRIVEN_DESIGN = new Book("5000", "Domain-Driven Design: Tackling Complexity in the Heart of Software", List.of("Eric Evans"), 2004, new BigDecimal("31.07"));
    public static final Book LITTLE_SCHEMER = new Book("6000", "The Little Schemer", List.of("Daniel P. Friedman", "Matthias Felleisen"), 1995, new BigDecimal("35.49"));
    public static final Book DATA_INTENSIVE_APPLICATIONS = new Book("7000", "Designing Data-Intensive Applications", List.of("Martin Kleppman"), 2017, new BigDecimal("35.00"));
    public static final Book REFACTORING_TO_PATTERNS = new Book("100", "Refactoring to Patterns", List.of("Joshua Kerievsky"), 2004, new BigDecimal("41.99"));
    public static final Book PROGRAMMING_PEARLS = new Book("200", "Programming Pearls", List.of("Jon Bentley"), 1986, new BigDecimal("41.99"));

    public static Book.BookBuilder book() {
        return Book.builder()
                .id("1000")
                .title("Title")
                .authors(List.of("First Author", "Second Author"))
                .year(2021)
                .price(new BigDecimal("19.99"));
    }

    public static Order.OrderBuilder order() {
        return Order.builder()
                .orderId("100")
                .status(Order.Status.IN_PROGRESS)
                .items(List.of(new OrderItem(DOMAIN_DRIVEN_DESIGN, 2),
                               new OrderItem(PROGRAMMING_PEARLS, 1)));
    }
}
