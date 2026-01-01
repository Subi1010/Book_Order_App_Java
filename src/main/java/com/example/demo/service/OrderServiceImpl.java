package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderServiceImpl implements  OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
  private final OrderRepository orderRepository;
  private final BookService bookService;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository, BookService bookService) {
      this.orderRepository = orderRepository;
      this.bookService = bookService;
  }

  @Override
  public Order placeOrder(String bookId, int quantity, String customerName) {
      // Check if the book exists
      logger.info("Placing order for book ID: {}, quantity: {}, customer: {}", bookId, quantity, customerName);
      var bookOpt = bookService.findById(bookId);
      if (bookOpt.isEmpty()) {
          logger.error("Failed to place order - Book with ID {} does not exist", bookId);
          throw new IllegalArgumentException("Book with ID " + bookId + " does not exist.");
      }

      // Create and save the order
      Order order = new Order(bookId, quantity, customerName);
      Order savedOrder = orderRepository.save(order);
      logger.info("Order placed successfully with ID: {}", savedOrder.getId());
      return savedOrder;
    }

  @Override
  public List<Order> getAllOrders() {
      logger.debug("Fetching all orders from repository");
      List<Order> orders = orderRepository.findAll();
      logger.debug("Found {} orders", orders.size());
      return orders;
  }

  @Override
  public Optional<Order> getOrderById(String id) {
      logger.debug("Fetching order with ID: {}", id);
      Optional<Order> order = orderRepository.findById(id);
      if (order.isPresent()) {
          logger.debug("Order found with ID: {}", id);
      } else {
          logger.debug("Order not found with ID: {}", id);
      }
      return order;
  }
  }
