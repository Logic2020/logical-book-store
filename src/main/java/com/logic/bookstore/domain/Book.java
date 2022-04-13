package com.logic.bookstore.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class Book {

    // external id (barcode)
    private String id;

    private String title;
    private List<String> authors;
    private int year;

    private BigDecimal price;

    public Book(String id, String title, List<String> authors, int year, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.price = price;
    }
}
