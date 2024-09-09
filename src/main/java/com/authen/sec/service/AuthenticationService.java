package com.authen.sec.service;

import com.authen.sec.model.authentication.User;
import com.authen.sec.repository.UserRepository;
import com.authen.sec.security.JwtUtil;
import com.authen.sec.model.blockchain.Blockchain;
import com.authen.sec.model.blockchain.Block;
import com.authen.sec.model.blockchain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Blockchain blockchain;
    private final EmailService emailService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, Blockchain blockchain, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.blockchain = blockchain;
        this.emailService = emailService;
    }

    @PostConstruct
    public void init() {
        try {
            if (userRepository.count() == 0) {
                logger.info("Initializing default users");
                registerUser("john_doe", "john@example.com", "password123", false);
                registerUser("jane_smith", "jane@example.com", "securePass", false);
                registerUser("admin", "admin@example.com", "adminpass", true);
            }
        } catch (Exception e) {
            logger.error("Error initializing default users", e);
        }
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username) != null;
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    public void registerUser(String username, String email, String password, boolean isAdmin) {
        logger.info("Attempting to register user: {}", username);
        if (isUsernameTaken(username)) {
            logger.warn("Username {} is already taken", username);
            throw new RuntimeException("Username is already taken. Please choose another.");
        }
        if (isEmailTaken(email)) {
            logger.warn("Email {} is already registered", email);
            throw new RuntimeException("Email is already registered. Please use a different email.");
        }
        try {
            String salt = generateSalt();
            String encodedPassword = passwordEncoder.encode(password + salt);
            String role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";

            User newUser = new User(username, email, encodedPassword, "", role);
            newUser.setSalt(salt);
// In the registerUser method of AuthenticationService.java
String emailConfirmationToken = UUID.randomUUID().toString();
    newUser.setEmailConfirmationToken(emailConfirmationToken);
    LocalDateTime expiryDate = LocalDateTime.now().plusHours(2);
    newUser.setEmailConfirmationTokenExpiryDate(expiryDate);
    logger.info("Setting email confirmation token expiry for user {} to: {}", username, expiryDate);
            newUser.setEmailConfirmed(false);
            
            try {
                newUser.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error generating key pair", e);
            }

            User savedUser = userRepository.save(newUser);

            Transaction transaction = new Transaction("SYSTEM", savedUser.getPublicKey(), 0);
            transaction.signTransaction(savedUser.getPrivateKey());

            Block newBlock = new Block(blockchain.getLatestBlock().getHash());
            newBlock.addTransaction(transaction);
            blockchain.addBlock(newBlock);

            emailService.sendConfirmationEmail(email, emailConfirmationToken, username);

            logger.info("User {} registered successfully with role {}", username, role);
        } catch (Exception e) {
            logger.error("Error registering user: {}", username, e);
            throw new RuntimeException("Error registering user", e);
        }
    }

    @Transactional
    public void confirmEmail(String token) {
        User user = userRepository.findByEmailConfirmationToken(token);
        if (user == null) {
            logger.error("No user found with confirmation token: {}", token);
            throw new RuntimeException("Invalid confirmation token");
        }
        
        logger.info("Found user {} with token. Email confirmed: {}, Expiry date: {}", 
                    user.getUsername(), user.isEmailConfirmed(), user.getEmailConfirmationTokenExpiryDate());
        
        if (user.isEmailConfirmed()) {
            logger.info("Email already confirmed for user: {}", user.getUsername());
            throw new RuntimeException("Email already confirmed");
        }
        
        if (user.getEmailConfirmationTokenExpiryDate().isBefore(LocalDateTime.now())) {
            logger.error("Expired confirmation token for user: {}. Expiry: {}, Current time: {}", 
                         user.getUsername(), user.getEmailConfirmationTokenExpiryDate(), LocalDateTime.now());
            throw new RuntimeException("Expired confirmation token");
        }
        
        user.setEmailConfirmed(true);
        user.setEmailConfirmationToken(null);
        user.setEmailConfirmationTokenExpiryDate(null);
        userRepository.save(user);
        logger.info("Email confirmed for user: {}", user.getUsername());
    }
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Password reset requested for non-existent email: {}", email);
            throw new RuntimeException("If a user with this email exists, a password reset link will be sent.");
        }
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiryDate(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
        emailService.sendPasswordResetEmail(email, resetToken);
        logger.info("Password reset requested for user: {}", user.getUsername());
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token);
        if (user == null || user.getPasswordResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired reset token");
        }
        String salt = generateSalt();
        String encodedPassword = passwordEncoder.encode(newPassword + salt);
        user.setPasswordHash(encodedPassword);
        user.setSalt(salt);
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiryDate(null);
        userRepository.save(user);
        logger.info("Password reset successful for user: {}", user.getUsername());
    }

    public String authenticateUser(String username, String password) {
        logger.info("Attempting to authenticate user: {}", username);
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                logger.warn("User not found: {}", username);
                throw new RuntimeException("Invalid username or password");
            }
    
            if (!user.isEmailConfirmed()) {
                logger.warn("Email not confirmed for user: {}", username);
                throw new RuntimeException("Please confirm your email before logging in");
            }
    
            String saltedPassword = password + user.getSalt();
            if (passwordEncoder.matches(saltedPassword, user.getPasswordHash())) {
                logger.info("Password matched for user: {}", username);
                String token = jwtUtil.generateToken(username, user.getRole());
                logger.info("Generated token for user: {} with role: {}", username, user.getRole());
                return token;
            }
            logger.warn("Password mismatch for user: {}", username);
            throw new RuntimeException("Invalid username or password");
        } catch (Exception e) {
            logger.error("Error authenticating user: {}", username, e);
            throw new RuntimeException("Error during authentication: " + e.getMessage());
        }
    }

    public String getUserPublicKey(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.debug("Public key request: User not found: {}", username);
            return null;
        }
        return user.getPublicKey();
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}