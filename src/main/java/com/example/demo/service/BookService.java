package com.example.demo.service;


import java.util.List;
import java.util.Optional;

import com.example.demo.model.Book;

public interface BookService {
    Book save(Book book);
    List<Book> findAll();
    Optional<Book> findById(String id);
}
