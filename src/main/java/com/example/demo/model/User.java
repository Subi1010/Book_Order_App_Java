package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User {
  @Id
  private String id;

  private String username;

  private String password;

  private String[] roles;

  public User(String username, String password, String[] roles) {
      this.roles = roles;
      this.username = username;
      this.password = password;

  }
}
