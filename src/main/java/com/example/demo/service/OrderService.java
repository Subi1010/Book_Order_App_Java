package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.PaymentResponse;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;

public interface OrderService {
Order placeOrder(List<OrderItem> items);
PaymentResponse initiatePayment(String orderId);
List<Order> getAllOrders();
Optional<Order> getOrderById(String id);
}
