package com.logic.bookstore.repository;

import com.logic.bookstore.domain.InventoryItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import com.logic.bookstore.exception.RepositoryException;


public interface InventoryRepository {

    List<InventoryItem> findAll() throws RepositoryException;

    Optional<InventoryItem> findById(String bookId) throws RepositoryException;

    InventoryItem create(InventoryItem item) throws RepositoryException;

    InventoryItem increaseAmount(String bookId, int amountToAdd) throws RepositoryException;

    InventoryItem decreaseAmount(String bookId, int amount) throws RepositoryException;

    void delete(String bookId) throws RepositoryException;
}
