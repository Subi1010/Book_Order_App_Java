package com.example.demo.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "orders")
@Data
@NoArgsConstructor
public class Order {
@Id
private String id;
private String bookId;
private int quantity;
private String customerName;
private LocalDateTime orderDate;

public Order(String bookId, int quantity, String customerName) {
    this.bookId = bookId;
    this.quantity = quantity;
    this.customerName = customerName;
    this.orderDate = LocalDateTime.now();
}
}
