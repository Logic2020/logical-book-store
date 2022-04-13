package com.logic.bookstore.rest;

import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.rest.request.AddInventoryRequest;
import com.logic.bookstore.service.InventoryService;
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

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("")
    public List<InventoryItem> listInventory() {
        return inventoryService.getAllInventory();
    }

    @PostMapping("")
    public InventoryItem addMoreItems(@RequestBody AddInventoryRequest request) {
        return inventoryService.createOrUpdate(request.getBookId(), request.getAmount());
    }

    @GetMapping("/{id}")
    public InventoryItem getInventoryItem(@PathVariable String id) {
        return inventoryService.getInventoryFor(id);
    }
}
