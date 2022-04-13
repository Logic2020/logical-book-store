package com.logic.bookstore.repository;

import com.logic.bookstore.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Book> books;

    public InMemoryProductRepository() {
        books = new HashMap<>();
    }

    @Override
    public Book save(Book book) {
        books.put(book.getId(), book);
        return book;
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    @Override
    public Optional<Book> findById(String id) {
        if (!books.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(books.get(id));
    }

    @Override
    public void delete(String id) {
        books.remove(id);
    }
}
