package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {

  private String bookId;
  private int quantity;

  public OrderItem(String bookId, int quantity) {
      this.bookId = bookId;
      this.quantity = quantity;
  }

}
