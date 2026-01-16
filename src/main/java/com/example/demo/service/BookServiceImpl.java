package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(String title, String author, int price) {
        // Any pre-save business logic can go here
        logger.info("Saving book: {}", title);
        //book.setQuantity(1); // If the field is ignored and no default given, for int it will be 0 (which will be saved in MongoDB) and for objects null which will not be saved in MongoDB
        Book book = new Book(title,author,price);
        Book savedBook = bookRepository.save(book);
        logger.info("Book saved successfully with ID: {}", savedBook.getId());
        return savedBook;
    }

    @Override
    public List<Book> findAll() {
        // Any pre-retrieval business logic can go here
        logger.debug("Fetching all books from repository");
        List<Book> books = bookRepository.findAll();
        logger.debug("Found {} books", books.size());
        return books;
    }

    @Override
    public Optional<Book> findById(String id) {
        // Any pre-retrieval business logic can go here
        logger.debug("Fetching book with ID: {}", id);
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            logger.debug("Book found with ID: {}", id);
        } else {
            logger.debug("Book not found with ID: {}", id);
        }
        return book;
    }
}
