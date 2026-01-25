package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PaymentWebhookRequest;
import com.example.demo.service.PaymentWebhookService;


@RestController
@RequestMapping("/webhook")
public class PaymentWebhookController {

    private final PaymentWebhookService webhookService;

    public PaymentWebhookController(PaymentWebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/payment")
    public ResponseEntity<Void> handlePaymentWebhook(
            @RequestBody PaymentWebhookRequest request,
            @RequestHeader("X-Signature") String signature // example
    ) {
        webhookService.processPaymentWebhook(request, signature);
        return ResponseEntity.ok().build();
    }
}
