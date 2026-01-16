package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.security.SecurityUtil;

@Service
public class OrderServiceImpl implements  OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
  private final OrderRepository orderRepository;
  private final BookService bookService;
  private final SecurityUtil securityUtil;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository, BookService bookService, SecurityUtil securityUtil) {
      this.orderRepository = orderRepository;
      this.bookService = bookService;
      this.securityUtil = securityUtil;
  }

  @Override
  public Order placeOrder(String bookId, int quantity) {
      // Check if the book exists
      logger.info("Placing order for book ID: {}, quantity: {}, customer: {}", bookId, quantity);
      var bookOpt = bookService.findById(bookId);
      if (bookOpt.isEmpty()) {
          logger.error("Failed to place order - Book with ID {} does not exist", bookId);
          throw new IllegalArgumentException("Book with ID " + bookId + " does not exist.");
      }

      // Create and save the order
      Order order = new Order(bookId, quantity);
      String userId= securityUtil.getCurrentUserId();
      order.setUserId(userId);
      Order savedOrder = orderRepository.save(order);
      logger.info("Order placed successfully with ID: {}", savedOrder.getId());
      return savedOrder;
    }

  @Override
  public List<Order> getAllOrders() {
      logger.debug("Fetching all orders from repository");
      String userId = securityUtil.getCurrentUserId();
      List<Order> orders = orderRepository.findByUserId(userId);
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
