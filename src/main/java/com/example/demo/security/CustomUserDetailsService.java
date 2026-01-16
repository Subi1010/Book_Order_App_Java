package com.example.demo.security;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from MongoDB
        logger.debug("Loading user details for username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
user.getId();
        logger.debug("User details loaded successfully for username: {}", username);
        // Return a Spring Security UserDetails object
        /* return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // hashed password
                .roles(user.getRoles())        // role(s) .roles() --> will automatically convert role into ROLE_ for authority to understand
                .build(); */
          return new CustomUserDetails(
            user.getId(),           // ✅ user_id added
            user.getUsername(),
            user.getPassword(),
            Arrays.stream(user.getRoles())
                  .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                  .toList()
    );
    }
}
