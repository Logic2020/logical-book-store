package com.logic.bookstore.repository;

import com.logic.bookstore.domain.Book;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(String id);

    void delete(String id);
}
