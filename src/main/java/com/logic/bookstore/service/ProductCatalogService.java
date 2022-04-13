package com.logic.bookstore.service;

import com.logic.bookstore.domain.Book;
import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.exception.ExceptionFactory;
import com.logic.bookstore.repository.InventoryRepository;
import com.logic.bookstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductCatalogService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public ProductCatalogService(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @PostConstruct
    public void prepopulateBooks() {
        add(new Book("1000", "Refactoring: Improving the Design of Existing Code", List.of("Martin Fowler"), 1999, new BigDecimal("47.96")));
        add(new Book("2000", "C Programming Language", List.of("Kernighan Brian W", "Ritchie Dennis"), 1978, new BigDecimal("38.89")));
        add(new Book("3000", "Design Patterns: Elements of Reusable Object-Oriented Software", List.of("Gamma Erich", "Helm Richard", "Johnson Ralph", "Vlissides John"), 1994, new BigDecimal("32.39")));


        inventoryRepository.increaseAmount("1000", 10);
        inventoryRepository.increaseAmount("2000", 10);
    }

    public List<Book> getAllBooks() {
        return productRepository.findAll();
    }

    public Book getBook(String id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> ExceptionFactory.bookNotFound(id));
    }

    public Book add(Book book) {
        requireBookDoesNotExist(book.getId());
        productRepository.save(book);
        inventoryRepository.create(new InventoryItem(book.getId(), 0));
        return book;
    }

    public void removeBook(String id) {
        requireBookExists(id);
        inventoryRepository.delete(id);
        productRepository.delete(id);
    }

    public boolean exists(String id) {
        return productRepository.findById(id)
                .isPresent();
    }

    private void requireBookExists(String bookId) {
        if (!exists(bookId)) {
            throw ExceptionFactory.bookNotFound(bookId);
        }
    }

    private void requireBookDoesNotExist(String id) {
        if (exists(id)) {
            throw ExceptionFactory.bookAlreadyExists(id);
        }
    }
}
