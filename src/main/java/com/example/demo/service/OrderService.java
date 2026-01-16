package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Order;

public interface OrderService {
Order placeOrder(String bookId, int quantity);
List<Order> getAllOrders();
Optional<Order> getOrderById(String id);
}
