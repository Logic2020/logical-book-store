package com.logic.bookstore.rest;

import com.logic.bookstore.domain.Book;
import com.logic.bookstore.rest.request.CreateBookRequest;
import com.logic.bookstore.service.ProductCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ProductCatalogService productCatalog;

    @Autowired
    public ProductController(ProductCatalogService productCatalog) {
        this.productCatalog = productCatalog;
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        List<Book> books = productCatalog.getAllBooks();
        LOG.info("Retrieving all books from product catalog: {}", books);
        return books;
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@RequestBody CreateBookRequest createBookRequest) {
        Book book = createBookRequest.asBook();
        LOG.info("Adding new book to product catalog: {}", book);
        return productCatalog.add(book);
    }

    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable String id) {
        Book book = productCatalog.getBook(id);
        LOG.info("Retrieved book by id {} from product catalog: {}", id, book);
        return book;
    }

    @DeleteMapping("/books/{id}")
    public void removeBook(@PathVariable String id) {
        LOG.info("Deleting book from product catalog: {}", id);
        productCatalog.removeBook(id);
    }
}
