package com.logic.bookstore.rest;

import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.rest.request.AddInventoryRequest;
import com.logic.bookstore.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/inventory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class InventoryController {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping()
    public List<InventoryItem> listInventory() {
        List<InventoryItem> inventory = inventoryService.getAllInventory();
        LOG.info("Returning all existing inventory: {}", inventory);
        return inventory;
    }

    @PostMapping()
    public InventoryItem addMoreItems(@RequestBody AddInventoryRequest request) {
        InventoryItem item = inventoryService.createOrUpdate(request.getBookId(), request.getAmount());
        LOG.info("Adding {} more books with id: {}", request.getAmount(), request.getBookId());
        return item;
    }

    @GetMapping("/{id}")
    public InventoryItem getInventoryItem(@PathVariable String id) {
        InventoryItem item = inventoryService.getInventoryFor(id);
        LOG.info("Retrieved inventory item by id {} from inventory: {}", id, item);
        return item;
    }
}
