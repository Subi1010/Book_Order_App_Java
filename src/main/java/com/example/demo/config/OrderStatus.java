package com.example.demo.config;

public enum OrderStatus {
    PENDING,      // order created
    PAYMENT_INITIATED,
    COMPLETED,    // payment success
    FAILED        // payment failed
}
