package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

  @PostMapping
  public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest order) {
  logger.info("Placing order for book ID: {}, quantity: {}, customer: {}",
              order.bookId, order.quantity);
  try {
            Order newOrder = orderService.placeOrder(order.bookId, order.quantity);
            logger.info("Order placed successfully with ID: {}", newOrder.getId());
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to place order: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //TODO: handle bookId not found error
        }
}

@PostMapping("/{orderId}/pay")
  public ResponseEntity<PaymentResponse> pay(@PathVariable String orderId) {
    PaymentResponse response = orderService.initiatePayment(orderId);
    return ResponseEntity.ok(response);
    }

@GetMapping
  public List<Order> getAllOrders() {
    logger.info("Retrieving all orders");
    List<Order> orders = orderService.getAllOrders();
    logger.info("Retrieved {} orders", orders.size());
    return orders;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable String id) {
    logger.info("Retrieving order with ID: {}", id);
    Optional<Order> order = orderService.getOrderById(id);
    if (order.isPresent()) {
        logger.info("Order found with ID: {}", id);
        return ResponseEntity.ok(order.get());
    } else {
        logger.warn("Order not found with ID: {}", id);
        return ResponseEntity.notFound().build();
    }
  }
}
