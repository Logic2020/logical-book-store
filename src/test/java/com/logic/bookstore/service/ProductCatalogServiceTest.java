package com.logic.bookstore.service;

import com.logic.bookstore.TestDataBuilder;
import com.logic.bookstore.domain.Book;
import com.logic.bookstore.domain.InventoryItem;
import com.logic.bookstore.exception.ServiceException;
import com.logic.bookstore.repository.InventoryRepository;
import com.logic.bookstore.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.logic.bookstore.TestDataBuilder.DOMAIN_DRIVEN_DESIGN;
import static com.logic.bookstore.TestDataBuilder.LITTLE_SCHEMER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class ProductCatalogServiceTest {

    // under test
    private ProductCatalogService catalogService;

    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        inventoryRepository = mock(InventoryRepository.class);
        catalogService = new ProductCatalogService(productRepository, inventoryRepository);
    }

    @Test
    void getAllBooksReturnsEmptyListWhenCatalogIsEmpty() {
        // given
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        List<Book> books = catalogService.getAllBooks();
        // then
        assertThat(books.isEmpty(), is(true));
    }

    @Test
    void getAllBooksReturnsBooksPresentInCatalog() {
        // given
        List<Book> booksInCatalog = List.of(DOMAIN_DRIVEN_DESIGN, LITTLE_SCHEMER);
        when(productRepository.findAll()).thenReturn(booksInCatalog);
        // when
        List<Book> result = catalogService.getAllBooks();
        // then
        assertThat(result.containsAll(booksInCatalog), is(true));
    }

    @Test
    void getBookByIdReturnsBookIfExists() {
        // given
        Book book = Book.builder()
                .id("1000")
                .title("Title")
                .authors(List.of("First Author", "Second Author"))
                .year(2021)
                .price(new BigDecimal("19.99")).build();
        when(productRepository.findById("1000")).thenReturn(Optional.of(book));
        // when
        Book result = catalogService.getBook("1000");
        // then
        assertThat(result, is(book));
    }

    @Test
    void addBookAddsBookToCatalogIfBookDoesNotExistAndCreatesEmptyInventory() {
        // given
        Book book = TestDataBuilder.book().id("1000").build();
        when(productRepository.findById("1000")).thenReturn(Optional.empty());
        // when
        Book addedBook = catalogService.add(book);
        // then
        assertThat(addedBook, is(book));
        verify(productRepository, times(1)).save(book);
        verify(inventoryRepository, times(1)).create(new InventoryItem("1000", 0));
    }

    @Test
    void removeBookRemovesBookFromCatalogAndClearsInventory() {
        // given
        Book book = TestDataBuilder.book().id("1000").build();
        when(productRepository.findById("1000")).thenReturn(Optional.of(book));
        // when
        catalogService.removeBook("1000");
        // then
        verify(productRepository, times(1)).delete("1000");
        verify(inventoryRepository, times(1)).delete("1000");
    }

    @Test
    void existsReturnsTrueIfBookWithGivenIdIsPresentInCatalog() {
        // given
        Book book = TestDataBuilder.book().id("1000").build();
        when(productRepository.findById("1000")).thenReturn(Optional.of(book));
        // when
        boolean result = catalogService.exists("1000");
        // then
        assertThat(result, is(true));
    }

    @Test
    void existsReturnsFalseIfBookWithGivenIdDoesNotExistInCatalog() {
        // given
        when(productRepository.findById("1000")).thenReturn(Optional.empty());
        // when
        boolean result = catalogService.exists("1000");
        // then
        assertThat(result, is(false));
    }

    @Test
    void attemptToGetNonExistentBookThrowsException() {
        // given
        when(productRepository.findById("1000")).thenReturn(Optional.empty());
        // when
        // then
        ServiceException exception = assertThrowsExactly(ServiceException.class,
                                                         () -> catalogService.getBook("1000"));
        assertThat(exception.getMessage(), is("Book not found. Book ID: " + 1000));
        assertThat(exception.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void attemptToCreateBookWithAlreadyExistingIdThrowsException() {
        Book book = TestDataBuilder.book().id("1000").build();
        when(productRepository.findById("1000")).thenReturn(Optional.of(book));
        // when
        ServiceException exception = assertThrowsExactly(ServiceException.class,
                                                         () -> catalogService.add(book));
        assertThat(exception.getMessage(), is("Book already exists. Book ID: " + 1000));
        assertThat(exception.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void attemptToDeleteBookThatDoesNotExistThrowsException() {
        // given
        when(productRepository.findById("1000")).thenReturn(Optional.empty());
        // when
        ServiceException exception = assertThrowsExactly(ServiceException.class,
                                                         () -> catalogService.removeBook("1000"));
        assertThat(exception.getMessage(), is("Book not found. Book ID: " + 1000));
        assertThat(exception.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
