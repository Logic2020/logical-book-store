package com.logic.bookstore.service;

import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.exception.ExceptionFactory;
import com.logic.bookstore.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductCatalogService productCatalog;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository,
                            ProductCatalogService productCatalog) {
        this.inventoryRepository = inventoryRepository;
        this.productCatalog = productCatalog;
    }

    public List<InventoryItem> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public InventoryItem createOrUpdate(String bookId, int amount) {
        if (!productCatalog.exists(bookId)) {
            throw ExceptionFactory.bookNotFound(bookId);
        }

        inventoryRepository
                .findById(bookId)
                .ifPresentOrElse(item -> inventoryRepository.increaseAmount(bookId, amount),
                                 () -> inventoryRepository.create(new InventoryItem(bookId, amount)));

        return getInventoryFor(bookId);
    }

    public InventoryItem getInventoryFor(String bookId) {
        return inventoryRepository
                .findById(bookId)
                .orElseThrow(() -> ExceptionFactory.inventoryNotFound(bookId));
    }

    public void decreaseAmount(String bookId, int amount) {
        inventoryRepository.findById(bookId)
                .ifPresent(inventory -> inventoryRepository.decreaseAmount(bookId, amount));
    }

    public void ensureEnoughInventory(Map<String, Integer> bookIdsToQuantities) {
        for (Map.Entry<String, Integer> entry : bookIdsToQuantities.entrySet()) {
            String bookId = entry.getKey();
            int quantity = entry.getValue();
            if (!hasEnough(bookId, quantity)) {
                throw ExceptionFactory.notEnoughBooks(bookId);
            }
        }
    }

    private boolean hasEnough(String bookId, int amount) {
        InventoryItem item = getInventoryFor(bookId);
        return item.getAmount() >= amount;
    }
}
