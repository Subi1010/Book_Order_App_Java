package com.example.demo.security;

import org.springframework.stereotype.Component;

import com.example.demo.dto.PaymentWebhookRequest;

@Component
public class WebhookSignatureVerifier {

    public boolean verify(PaymentWebhookRequest request, String signature) {
        // In real gateway:
        // - compute HMAC
        // - compare with signature
        return true; // stub for now
    }
}
