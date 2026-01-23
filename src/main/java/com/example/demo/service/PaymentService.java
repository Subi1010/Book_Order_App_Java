package com.example.demo.service;

import com.example.demo.dto.PaymentResponse;
import com.example.demo.model.Order;

public interface PaymentService {
  PaymentResponse initiatePayment(Order order);
}
