package com.logic.bookstore.repository;

import com.logic.bookstore.domain.Order;
import com.logic.bookstore.exception.RepositoryException;
import com.logic.bookstore.exception.RepositoryExceptionFactory;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryOrderRepositoryExceptionSimulator implements OrderRepository {

    public InMemoryOrderRepositoryExceptionSimulator() {
    }

    @Override
    public Order save(Order order) throws RepositoryException {
        throw RepositoryExceptionFactory.networkError();
    }

    @Override
    public Optional<Order> findById(String id) throws RepositoryException  {
        throw RepositoryExceptionFactory.databaseError();
    }

    @Override
    public List<Order> findAll() throws RepositoryException {
        throw RepositoryExceptionFactory.databaseError();
    }
}
