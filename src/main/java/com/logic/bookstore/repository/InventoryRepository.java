package com.logic.bookstore.repository;

import com.logic.bookstore.domain.InventoryItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface InventoryRepository {

    List<InventoryItem> findAll();

    Optional<InventoryItem> findById(String bookId);

    InventoryItem create(InventoryItem item);

    InventoryItem increaseAmount(String bookId, int amountToAdd);

    InventoryItem decreaseAmount(String bookId, int amount);

    void delete(String bookId);
}
