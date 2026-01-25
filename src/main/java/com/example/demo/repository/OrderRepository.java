package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
  List<Order> findByUserId(String userId);
  Optional<Order> findByPaymentId(String paymentId);
}
