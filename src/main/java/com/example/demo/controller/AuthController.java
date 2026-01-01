package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
      logger.info("Registration request received for username: {}", request.username);
      try {
          authService.register(request);
          logger.info("User registered successfully: {}", request.username);
      } catch (RuntimeException e) {
          logger.error("Registration failed for username: {}: {}", request.username, e.getMessage());
          return ResponseEntity.badRequest().body(e.getMessage());
      }
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
      logger.info("Login request received for username: {}", request.username);
      AuthResponse token;
      try
      {
      token = authService.login(request);
      logger.info("User logged in successfully: {}", request.username);
      } catch (RuntimeException e) {
          logger.error("Login failed for username: {}: {}", request.username, e.getMessage());
          return ResponseEntity.status(401).body(new AuthResponse("Error:"+e.getMessage()));
      }
        return ResponseEntity.ok(token);
    }
}
