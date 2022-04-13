package com.logic.bookstore.repository;

import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.exception.ExceptionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryInventoryRepository implements InventoryRepository {

    private final Map<String, InventoryItem> inventory;

    public InMemoryInventoryRepository() {
        this.inventory = new HashMap<>();
    }

    @Override
    public List<InventoryItem> findAll() {
        return new ArrayList<>(inventory.values());
    }

    @Override
    public Optional<InventoryItem> findById(String bookId) {
        if (!inventory.containsKey(bookId)) {
            return Optional.empty();
        }

        return Optional.of(inventory.get(bookId));
    }

    @Override
    public InventoryItem create(InventoryItem item) {
        if (inventory.containsKey(item.getBookId())) {
            throw ExceptionFactory.inventoryAlreadyExists(item.getBookId());
        }

        inventory.put(item.getBookId(), item);
        return item;
    }

    @Override
    public InventoryItem increaseAmount(String bookId, int amountToAdd) {
        if (!inventory.containsKey(bookId)) {
            throw ExceptionFactory.inventoryNotFound(bookId);
        }

        InventoryItem item = inventory.get(bookId);
        item.setAmount(item.getAmount() + amountToAdd);
        return inventory.get(bookId);
    }

    @Override
    public InventoryItem decreaseAmount(String bookId, int amount) {
        if (!inventory.containsKey(bookId)) {
            throw ExceptionFactory.inventoryNotFound(bookId);
        }

        InventoryItem item = inventory.get(bookId);
        item.setAmount(item.getAmount() - amount);
        return inventory.get(bookId);
    }

    @Override
    public void delete(String bookId) {
        inventory.remove(bookId);
    }
}
