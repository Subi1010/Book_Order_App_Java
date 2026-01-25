package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.OrderStatus;
import com.example.demo.dto.PaymentWebhookRequest;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.security.WebhookSignatureVerifier;

@Service
public class PaymentWebhookService {

    private final OrderRepository orderRepository;
    private final WebhookSignatureVerifier signatureVerifier;

    public PaymentWebhookService(OrderRepository orderRepository,WebhookSignatureVerifier signatureVerifier) {
        this.orderRepository = orderRepository;
        this.signatureVerifier = signatureVerifier;
    }

    @Transactional
    public void processPaymentWebhook(PaymentWebhookRequest request, String signature) {

        // 1️⃣ Verify webhook authenticity
        if (!signatureVerifier.verify(request, signature)) {
            throw new SecurityException("Invalid webhook signature");
        }

        // 2️⃣ Find order by paymentId
        Order order = orderRepository.findByPaymentId(request.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 3️⃣ Idempotency check
        if (order.getStatus() == OrderStatus.COMPLETED) {
            return; // already processed
        }

        // 4️⃣ Amount validation (VERY IMPORTANT)
        if (order.getTotalAmount().compareTo(request.getAmount()) != 0) {
            throw new IllegalStateException("Payment amount mismatch");
        }

        // 5️⃣ Update order status
        switch (request.getStatus()) {
            case "SUCCESS" -> order.setStatus(OrderStatus.COMPLETED);
            case "FAILED" -> order.setStatus(OrderStatus.FAILED);
            default -> throw new IllegalArgumentException("Unknown payment status");
        }

        orderRepository.save(order);
    }
}
