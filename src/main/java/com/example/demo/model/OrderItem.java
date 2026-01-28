package com.example.demo.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {

  private String bookId;
  private int quantity;
  private BigDecimal price;

  public OrderItem(String bookId, int quantity, BigDecimal price) {
      this.bookId = bookId;
      this.quantity = quantity;
      this.price = price;
  }

}
