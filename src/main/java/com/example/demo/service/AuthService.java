package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtUtil;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest request) {
        logger.info("Attempting to register user: {}", request.username);
        if (userRepository.findByUsername(request.username).isPresent()) {
            logger.warn("Registration failed - username already taken: {}", request.username);
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.username);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setRoles(request.roles);
        userRepository.save(user);
        logger.info("User registered successfully: {}", request.username);
    }

    public AuthResponse login(LoginRequest request) {
        logger.info("Attempting to authenticate user: {}", request.username);
        UserDetails user = customUserDetailsService.loadUserByUsername(request.username);

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            logger.warn("Authentication failed - invalid credentials for user: {}", request.username);
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user); // server side token generation
        logger.info("User authenticated successfully: {}", request.username);
        return new AuthResponse(token);
    }
}
