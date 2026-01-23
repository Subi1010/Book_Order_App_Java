package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.PaymentResponse;
import com.example.demo.model.Order;

public interface OrderService {
Order placeOrder(String bookId, int quantity);
PaymentResponse initiatePayment(String orderId);
List<Order> getAllOrders();
Optional<Order> getOrderById(String id);
}
