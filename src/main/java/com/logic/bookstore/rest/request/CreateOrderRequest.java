package com.logic.bookstore.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderRequest {

    private final List<Item> items;

    @JsonCreator
    public CreateOrderRequest(@JsonProperty(value = "items", required = true) List<Item> items) {
        this.items = items;
    }

    public Map<String, Integer> orderedItems() {
        return items.stream().collect(Collectors.toMap(Item::getBookId, Item::getQuantity));
    }

    @Data
    public static class Item {

        private String bookId;
        private int quantity;

        @JsonCreator
        public Item(@JsonProperty(value = "bookId", required = true) String bookId,
                    @JsonProperty(value = "quantity", required = true) Integer quantity) {
            this.bookId = bookId;
            this.quantity = quantity;
        }
    }
}
