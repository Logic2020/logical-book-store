package com.logic.bookstore.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logic.bookstore.TestDataBuilder;
import com.logic.bookstore.domain.Book;
import com.logic.bookstore.exception.CustomExceptionHandler;
import com.logic.bookstore.exception.ExceptionFactory;
import com.logic.bookstore.rest.request.CreateBookRequest;
import com.logic.bookstore.service.ProductCatalogService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductCatalogService productCatalog;

    @Autowired
    CustomExceptionHandler customExceptionHandler;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper();
    }

    @Test
    void getAllBooksReturnsAllBooksFromProductCatalog() throws Exception {
        // given
        List<Book> books = List.of(TestDataBuilder.LITTLE_SCHEMER, TestDataBuilder.DATA_INTENSIVE_APPLICATIONS);
        when(productCatalog.getAllBooks()).thenReturn(books);
        // when
        MvcResult result = mockMvc.perform(
                get("/catalog/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        // then
        List<Book> returnedBooks = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(returnedBooks.containsAll(books), is(true));
    }

    @Test
    void createBookAddsNewBookToProductCatalog() throws Exception {
        // given
        Book book = TestDataBuilder.DOMAIN_DRIVEN_DESIGN;
        when(productCatalog.add(book)).thenReturn(book);
        CreateBookRequest bookRequest = CreateBookRequest.from(book);
        // when
        MvcResult result = mockMvc.perform(
                post("/catalog/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andReturn();
        // then
        Book savedBook = mapper.readValue(result.getResponse().getContentAsString(), Book.class);
        assertThat(savedBook, is(book));
    }

    @Test
    void gettingExistingBookByIdReturnsIt() throws Exception {
        // given
        Book book = TestDataBuilder.REFACTORING_TO_PATTERNS;
        when(productCatalog.getBook(book.getId())).thenReturn(book);
        // when
        MvcResult result = mockMvc.perform(
                get("/catalog/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        // then
        Book retrievedBook = mapper.readValue(result.getResponse().getContentAsString(), Book.class);
        assertThat(retrievedBook, is(book));
    }

    @Test
    void removeExistingBookDeletesItFromCatalog() throws Exception {
        // when
        mockMvc.perform(
                delete("/catalog/books/42")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        // then
        verify(productCatalog, times(1)).removeBook("42");
    }

    @Test
    void attemptToAddAlreadyExistingBookThrowsException() throws Exception {
        // given
        Book book = TestDataBuilder.DOMAIN_DRIVEN_DESIGN;
        when(productCatalog.add(book)).thenThrow(ExceptionFactory.bookAlreadyExists(book.getId()));
        CreateBookRequest bookRequest = CreateBookRequest.from(book);
        // when
        MvcResult result = mockMvc.perform(
                post("/catalog/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(bookRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        // then
        assertTrue(result.getResponse().getContentAsString().contains("Book already exists"));
    }

    @Test
    void attemptToGetNonexistentBookByIdThrowsException() throws Exception {
        // given
        String bookId = "999";
        when(productCatalog.getBook(eq(bookId))).thenThrow(ExceptionFactory.bookNotFound(bookId));
        // when
        // then
        MvcResult result = mockMvc.perform(
                get("/catalog/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Book not found"));
    }
}
