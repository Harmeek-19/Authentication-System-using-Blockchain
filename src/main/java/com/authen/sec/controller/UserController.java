package com.authen.sec.controller;

import com.authen.sec.dto.UserLoginDto;
import com.authen.sec.dto.UserRegistrationDto;
import com.authen.sec.model.authentication.User;
import com.authen.sec.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            logger.info("Received registration request for user: {}", registrationDto.getUsername());
            authenticationService.registerUser(registrationDto.getUsername(), registrationDto.getEmail(), registrationDto.getPassword(), false);
            logger.info("User registered successfully: {}", registrationDto.getUsername());
            return ResponseEntity.ok("User registered successfully. Please check your email to confirm your account.");
        } catch (Exception e) {
            logger.error("Error registering user: {}", registrationDto.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration: " + e.getMessage());
        }
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
        try {
            logger.info("Received email confirmation request with token: {}", token);
            authenticationService.confirmEmail(token);
            logger.info("Email confirmed successfully for token: {}", token);
            return ResponseEntity.ok("Email confirmed successfully. You can now log in.");
        } catch (RuntimeException e) {
            logger.error("Error confirming email for token: {}", token, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            authenticationService.requestPasswordReset(email);
            return ResponseEntity.ok("Password reset link sent to your email");
        } catch (Exception e) {
            logger.error("Error requesting password reset for email: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request");
        }
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");
        try {
            authenticationService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            logger.error("Error resetting password", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset token");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto) {
        try {
            logger.info("Received login request for user: {}", loginDto.getUsername());
            String token = authenticationService.authenticateUser(loginDto.getUsername(), loginDto.getPassword());
            logger.info("Login successful for user: {}", loginDto.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Login failed for user: {}", loginDto.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error during login for user: {}", loginDto.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    @GetMapping("/public-key")
    public ResponseEntity<?> getPublicKey(Authentication authentication) {
        try {
            String username = authentication.getName();
            logger.info("Fetching public key for user: {}", username);
            String publicKey = authenticationService.getUserPublicKey(username);
            if (publicKey != null) {
                return ResponseEntity.ok(publicKey);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Public key not found");
            }
        } catch (Exception e) {
            logger.error("Error fetching public key", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the public key");
        }
    }
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(Authentication authentication) {
        try {
            String username = authentication.getName();
            logger.info("Fetching dashboard data for user: {}", username);
            User user = authenticationService.getUserByUsername(username);
            if (user != null) {
                Map<String, Object> dashboardData = new HashMap<>();
                dashboardData.put("username", user.getUsername());
                dashboardData.put("email", user.getEmail());
                dashboardData.put("balance", 1000); // Replace with actual balance fetching logic
                dashboardData.put("recentTransactions", new ArrayList<>()); // Replace with actual transaction fetching logic
                return ResponseEntity.ok(dashboardData);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            logger.error("Error fetching dashboard data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching dashboard data");
        }
    }
}