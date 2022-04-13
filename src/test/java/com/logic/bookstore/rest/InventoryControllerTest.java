package com.logic.bookstore.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.exception.CustomExceptionHandler;
import com.logic.bookstore.exception.ExceptionFactory;
import com.logic.bookstore.rest.request.AddInventoryRequest;
import com.logic.bookstore.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class InventoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InventoryService inventoryService;

    @Autowired
    CustomExceptionHandler customExceptionHandler;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper();
    }

    @Test
    void listInventoryReturnsAllItemsInInventory() throws Exception {
        // given
        List<InventoryItem> inventory = List.of(new InventoryItem("1000", 10),
                                                new InventoryItem("2000", 5));
        when(inventoryService.getAllInventory()).thenReturn(inventory);
        // when
        MvcResult result = mockMvc.perform(
                get("/inventory")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        // then
        List<InventoryItem> returnedBooks = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(returnedBooks.containsAll(inventory), is(true));
    }

    @Test
    void addMoreItemsAddsMoreItemsToInventory() throws Exception {
        // given
        AddInventoryRequest request = new AddInventoryRequest("100", 10);
        InventoryItem expectedInventory = new InventoryItem("100", 20);
        when(inventoryService.createOrUpdate(eq("100"), eq(10))).thenReturn(expectedInventory);
        // when
        MvcResult result = mockMvc.perform(
                post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        // then
        InventoryItem returnedInventory = mapper.readValue(result.getResponse().getContentAsString(), InventoryItem.class);
        assertThat(returnedInventory, is(expectedInventory));
    }

    @Test
    void getInventoryItemReturnsInventoryItemById() throws Exception {
        // given
        InventoryItem itemsInInventory = new InventoryItem("100", 500);
        when(inventoryService.getInventoryFor(itemsInInventory.getBookId())).thenReturn(itemsInInventory);
        // when
        MvcResult result = mockMvc.perform(
                get("/inventory/" + itemsInInventory.getBookId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        // then
        InventoryItem returnedInventory = mapper.readValue(result.getResponse().getContentAsString(), InventoryItem.class);
        assertThat(returnedInventory, is(itemsInInventory));
    }

    @Test
    void attemptingToAddMoreItemsForNonExistentBookThrowsException() throws Exception {
        // given
        when(inventoryService.createOrUpdate(eq("100"), eq(42))).thenThrow(ExceptionFactory.bookNotFound("100"));
        // when
        MvcResult result = mockMvc.perform(
                post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(new AddInventoryRequest("100", 42))))
                .andExpect(status().isNotFound())
                .andReturn();
        // then
        assertTrue(result.getResponse().getContentAsString().contains("Book not found"));
    }

    @Test
    void attemptingToGetInventoryForNonExistentBookThrowsException() throws Exception {
        // given
        when(inventoryService.getInventoryFor("400")).thenThrow(ExceptionFactory.bookNotFound("400"));
        // when
        MvcResult result = mockMvc.perform(
                get("/inventory/400")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();
        // then
        assertTrue(result.getResponse().getContentAsString().contains("Book not found"));
    }
}
