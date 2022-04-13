package com.logic.bookstore.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.logic.bookstore.domain.Book;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookRequest {

    private String id;

    private String title;
    private List<String> authors;
    private int year;

    private BigDecimal price;

    @JsonCreator
    public CreateBookRequest(@JsonProperty(value = "id", required = true) String id,
                             @JsonProperty(value = "title", required = true) String title,
                             @JsonProperty(value = "authors", required = true) List<String> authors,
                             @JsonProperty(value = "year", required = true) int year,
                             @JsonProperty(value = "price", required = true) BigDecimal price) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.price = price;
    }

    public Book asBook() {
        return new Book(id, title, authors, year, price);
    }

    public static CreateBookRequest from(Book book) {
        return new CreateBookRequest(
                book.getId(),
                book.getTitle(),
                book.getAuthors(),
                book.getYear(),
                book.getPrice());
    }
}
