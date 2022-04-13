package com.logic.bookstore.repository;

import com.logic.bookstore.domain.Order;
import com.logic.bookstore.exception.RepositoryException;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order) throws RepositoryException;

    Optional<Order> findById(String id) throws RepositoryException;

    List<Order> findAll() throws RepositoryException;
}
