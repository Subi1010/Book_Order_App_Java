package com.example.demo.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
  private final JwtUtil jwtUtil;
  private final CustomUserDetailsService userDetailsService;

  public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
      this.jwtUtil = jwtUtil;
      this.userDetailsService = userDetailsService;
      logger.info("JWTAuthFilter initialized");
  }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        logger.info("=== JWTAuthFilter START: Processing request to {} ===", requestURI);

        // 1️⃣ Extract Authorization header
        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // 2️⃣ Extract token from header (Bearer <token>)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            logger.info("JWTAuthFilter: Bearer token found in Authorization header");
            try {
                username = jwtUtil.extractUsername(jwtToken);
                logger.info("JWTAuthFilter: Extracted username from JWT: {}", username);
            } catch (Exception e) {
                logger.error("JWTAuthFilter: Invalid JWT token: {}", e.getMessage());
            }
        } else {
            logger.info("JWTAuthFilter: No Bearer token found in request");
        }

        // 3️⃣ If we have a username and user not yet authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("JWTAuthFilter: Authenticating user: {}", username);

            // Load user details from DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 4️⃣ ✅ Validate the token here!
            if (jwtUtil.validateToken(jwtToken)) {
                logger.info("JWTAuthFilter: JWT token validated successfully for user: {}", username);

                // Create authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("JWTAuthFilter: Authentication set in SecurityContext for user: {}", username);
            } else {
                logger.warn("JWTAuthFilter: JWT token validation failed for user: {}", username);
            }
        }

        // 5️⃣ Continue filter chain
        logger.info("=== JWTAuthFilter END: Continuing filter chain for {} ===", requestURI);
        filterChain.doFilter(request, response);
        logger.info("=== JWTAuthFilter COMPLETE: Request to {} completed ===", requestURI);
    }

}
