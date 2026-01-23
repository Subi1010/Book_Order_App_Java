package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentResponse {

  private String paymentId;
  private String paymentUrl; // setters and getters are required when the variables are private as we cannot access outside, if the variables are public getters and setters are not needed

  public PaymentResponse(String paymentId, String paymentUrl) {
      this.paymentId = paymentId;
      this.paymentUrl = paymentUrl;
  }

  /* public String getPaymentId() {
      return paymentId;
  }
      // included default by Lombok

  public String getPaymentUrl() {
      return paymentUrl;
  } */
}
