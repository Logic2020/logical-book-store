package com.logic.bookstore.service;

import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.exception.ServiceException;
import com.logic.bookstore.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    private static final String BOOK_ID = "100";

    // under test
    private InventoryService inventoryService;

    private InventoryRepository repository;
    private ProductCatalogService productCatalog;

    @BeforeEach
    void setUp() {
        repository = mock(InventoryRepository.class);
        productCatalog = mock(ProductCatalogService.class);
        inventoryService = new InventoryService(repository, productCatalog);
    }

    @Test
    void getAllInventoryReturnsEmptyListIfNoInventoryIsPresent() {
        // given
        when(repository.findAll()).thenReturn(Collections.emptyList());
        // when
        List<InventoryItem> result = inventoryService.getAllInventory();
        // then
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void getAllInventoryReturnsItemsPresentInInventory() {
        // given
        List<InventoryItem> items = List.of(new InventoryItem("1000", 5),
                                            new InventoryItem("2000", 10));
        when(repository.findAll()).thenReturn(items);
        List<InventoryItem> result = inventoryService.getAllInventory();
        // then
        assertThat(result.containsAll(items), is(true));
    }

    @Test
    void createOrUpdateCreatesNewInventoryItemIfNotExists() {
        // given
        when(productCatalog.exists(BOOK_ID)).thenReturn(true);
        InventoryItem expectedNewInventory = new InventoryItem(BOOK_ID, 5);
        when(repository.findById(BOOK_ID))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(expectedNewInventory));
        when(repository.create(eq(new InventoryItem(BOOK_ID, 15)))).thenReturn(expectedNewInventory);
        // when
        InventoryItem createdInventory = inventoryService.createOrUpdate(BOOK_ID, 15);
        // then
        assertThat(createdInventory, is(expectedNewInventory));
    }

    @Test
    void createOrUpdateUpdatesAlreadyExistingInventory() {
        // given
        when(productCatalog.exists(BOOK_ID)).thenReturn(true);
        InventoryItem inventoryBeforeUpdate = new InventoryItem(BOOK_ID, 50);
        InventoryItem expectedInventory = new InventoryItem(BOOK_ID, 55);
        when(repository.findById(BOOK_ID))
                .thenReturn(Optional.of(inventoryBeforeUpdate))
                .thenReturn(Optional.of(expectedInventory));
        when(repository.increaseAmount(eq(BOOK_ID), eq(5))).thenReturn(expectedInventory);
        // when
        InventoryItem result = inventoryService.createOrUpdate(BOOK_ID, 5);
        // then
        assertThat(result, is(expectedInventory));
    }

    @Test
    void getInventoryReturnsInventoryIfExists() {
        // given
        InventoryItem expectedInventory = new InventoryItem(BOOK_ID, 100);
        when(repository.findById(BOOK_ID)).thenReturn(Optional.of(expectedInventory));
        // when
        InventoryItem result = inventoryService.getInventoryFor(BOOK_ID);
        // then
        assertThat(result, is(expectedInventory));
    }

    @Test
    void decreaseAmountReducesInventoryAmountForExistingInventory() {
        // given
        InventoryItem inventoryBeforeUpdate = new InventoryItem(BOOK_ID, 10);
        when(repository.findById(BOOK_ID))
                .thenReturn(Optional.of(inventoryBeforeUpdate));
        // when
        inventoryService.decreaseAmount(BOOK_ID, 5);
        // then
        verify(repository, times(1)).decreaseAmount(BOOK_ID, 5);
    }

    @Test
    void removingNoMoreThanExistingInventoryDoesNotThrowException() {
        // given
        String firstBookId = "1000";
        String secondBookId = "2000";
        when(repository.findById(firstBookId)).thenReturn(Optional.of(new InventoryItem(firstBookId, 10)));
        when(repository.findById(secondBookId)).thenReturn(Optional.of(new InventoryItem(secondBookId, 20)));
        Map<String, Integer> requestedQuantities = Map.of(firstBookId, 10, secondBookId, 18);
        // when
        inventoryService.ensureEnoughInventory(requestedQuantities);
        // then
        verify(repository, times(1)).findById(firstBookId);
        verify(repository, times(1)).findById(secondBookId);
    }

    @Test
    void attemptToRemoveMoreThanExistingInventoryThrowsException() {
        // given
        when(repository.findById(BOOK_ID))
                .thenReturn(Optional.of(new InventoryItem(BOOK_ID, 10)));
        Map<String, Integer> requestedQuantities = Map.of(BOOK_ID, 11);
        // when
        // then
        ServiceException exception = assertThrowsExactly(ServiceException.class,
                                                         () -> inventoryService.ensureEnoughInventory(requestedQuantities));
        assertThat(exception.getMessage(), is("Not enough books in inventory. Book ID: " + BOOK_ID));
        assertThat(exception.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void attemptToGetNonExistentInventoryThrowsException() {
        // given
        when(repository.findById(BOOK_ID))
                .thenReturn(Optional.empty());
        // when
        ServiceException exception = assertThrowsExactly(ServiceException.class,
                                                         () -> inventoryService.getInventoryFor(BOOK_ID));
        assertThat(exception.getMessage(), is("Inventory not found for book. Book ID: " + BOOK_ID));
        assertThat(exception.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void attemptToCreateInventoryForNonExistingBookThrowsException() {
        // given
        when(productCatalog.exists(BOOK_ID)).thenReturn(false);
        // when
        // then
        ServiceException exception = assertThrowsExactly(ServiceException.class,
                                                         () -> inventoryService.createOrUpdate(BOOK_ID, 25));
        assertThat(exception.getMessage(), is("Book not found. Book ID: " + BOOK_ID));
        assertThat(exception.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
