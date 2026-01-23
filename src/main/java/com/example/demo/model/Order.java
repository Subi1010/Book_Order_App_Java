package com.example.demo.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.config.OrderStatus;

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
private String userId;
private OrderStatus status;
private LocalDateTime orderDate;

public Order(String bookId, int quantity) {
    this.bookId = bookId;
    this.quantity = quantity;
    this.orderDate = LocalDateTime.now();
}
}
