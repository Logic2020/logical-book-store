package com.logic.bookstore.rest;

import com.logic.bookstore.domain.Book;
import com.logic.bookstore.rest.request.CreateBookRequest;
import com.logic.bookstore.service.ProductCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/catalog", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductCatalogController {

    private final ProductCatalogService catalogService;

    @Autowired
    public ProductCatalogController(ProductCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return catalogService.allBooks();
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@RequestBody CreateBookRequest createBookRequest) {
        return catalogService.add(createBookRequest.asBook());
    }

    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable String id) {
        return catalogService.getBook(id);
    }

    @DeleteMapping("/books/{id}")
    public void removeBook(@PathVariable String id) {
        catalogService.removeBook(id);
    }
}
