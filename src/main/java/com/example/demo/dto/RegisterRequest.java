package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class RegisterRequest {

  @NotBlank(message = "Username is required")
  public String username;

  @NotBlank(message = "Password is required")
  public String password;

  @NotEmpty(message = "At least one role is required")
  public String[] roles;
}
