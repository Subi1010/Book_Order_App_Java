package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
private List<OrderItem> items;
private String userId;
private OrderStatus status;
private BigDecimal totalAmount;
private LocalDateTime orderDate;

public Order(List<OrderItem> items) {
    this.items = items;
    this.orderDate = LocalDateTime.now();
}
}
