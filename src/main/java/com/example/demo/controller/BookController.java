package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.example.demo.dto.*;

/* @RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // Create a new book
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        book.setQuantity(1);
        return bookRepository.save(book);
    }

    // Retrieve all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Retrieve a book by ID
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable String id) {
        return bookRepository.findById(id).orElse(null);
    }
}
 */

@RestController
@RequestMapping("/api/books")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Create a new book
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Book createBook(@RequestBody BookRequest book) {
      logger.info("Creating book: {}", book.title);
        Book savedBook = bookService.save(book.title,book.author,book.price);
        logger.info("Book created successfully with ID: {}", savedBook.getId());
        return savedBook;
    }

    // Retrieve all books
    @GetMapping
    public List<Book> getAllBooks() {
        logger.info("Retrieving all books");
        List<Book> books = bookService.findAll();
        logger.info("Retrieved {} books", books.size());
        return books;
    }

    // Retrieve a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        logger.info("Retrieving book with ID: {}", id);
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            logger.info("Book found with ID: {}", id);
            return ResponseEntity.ok(book.get());
        } else {
            logger.warn("Book not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
