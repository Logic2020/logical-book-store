package com.logic.bookstore.service;

import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository,
                            ProductCatalogService productCatalog) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<InventoryItem> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public InventoryItem createOrUpdate(String bookId, int amount) {
        inventoryRepository.increaseAmount(bookId, amount);
        inventoryRepository.create(new InventoryItem(bookId, amount));
        return getInventoryFor(bookId);
    }

    public InventoryItem getInventoryFor(String bookId) {
        return inventoryRepository.findById(bookId).get();
    }

    public void decreaseAmount(String bookId, int amount) {
        inventoryRepository.decreaseAmount(bookId, amount);
    }
}
