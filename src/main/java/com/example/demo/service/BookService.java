package com.example.demo.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.example.demo.model.Book;

public interface BookService {
    Book save(String title, String author, BigDecimal price);
    List<Book> findAll();
    Optional<Book> findById(String id);
}
