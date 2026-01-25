package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentWebhookRequest {

  private String paymentId;
  private String orderId;
  private String status;
  private BigDecimal amount;

}