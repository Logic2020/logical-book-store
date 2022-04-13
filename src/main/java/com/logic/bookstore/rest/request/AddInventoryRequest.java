package com.logic.bookstore.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddInventoryRequest {

    private String bookId;
    private int amount;

    @JsonCreator
    public AddInventoryRequest(@JsonProperty(value = "bookId", required = true) String bookId,
                               @JsonProperty(value = "amount", required = true) int amount) {
        this.bookId = bookId;
        this.amount = amount;
    }
}
