package com.logic.bookstore.repository;

import com.logic.bookstore.domain.Book;

import java.util.List;
import java.util.Optional;
import com.logic.bookstore.exception.RepositoryException;


public interface ProductRepository {

    Book save(Book book) throws RepositoryException;

    List<Book> findAll() throws RepositoryException;

    Optional<Book> findById(String id) throws RepositoryException;

    void delete(String id) throws RepositoryException;
}
