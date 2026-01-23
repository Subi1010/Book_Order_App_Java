package com.example.demo.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PaymentResponse;
import com.example.demo.model.Order;

@Service
public class RazorpayPaymentService implements PaymentService {

  private static final Logger logger = LoggerFactory.getLogger(RazorpayPaymentService.class);

    @Override
    public PaymentResponse initiatePayment(Order order) {

        // Call Razorpay / Stripe API here


        // Simulating gateway response
        String paymentId = "pay_" + UUID.randomUUID();

        String paymentUrl =
                "https://checkout.razorpay.com/v1/checkout.js?payment_id="
                + paymentId;

        return new PaymentResponse(paymentId, paymentUrl);
    }
}
