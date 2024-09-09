package com.authen.sec.model.authentication;

import jakarta.persistence.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false, length = 2048)
    private String publicKey;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private boolean emailConfirmed = false;

    @Column
    private String emailConfirmationToken;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime emailConfirmationTokenExpiryDate;

    @Column
    private String passwordResetToken;

    @Column
    private LocalDateTime passwordResetTokenExpiryDate;

    @Column(nullable = false, length = 2048)
    private String privateKey;

    // Constructor, getters, and setters remain the same

    public User() {}

    public User(String username, String email, String passwordHash, String publicKey, String role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.publicKey = publicKey;
        this.role = role;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getEmailConfirmationToken() {
        return emailConfirmationToken;
    }

    public void setEmailConfirmationToken(String emailConfirmationToken) {
        this.emailConfirmationToken = emailConfirmationToken;
    }

    public LocalDateTime getEmailConfirmationTokenExpiryDate() {
        return emailConfirmationTokenExpiryDate;
    }

    public void setEmailConfirmationTokenExpiryDate(LocalDateTime emailConfirmationTokenExpiryDate) {
        this.emailConfirmationTokenExpiryDate = emailConfirmationTokenExpiryDate;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getPasswordResetTokenExpiryDate() {
        return passwordResetTokenExpiryDate;
    }

    public void setPasswordResetTokenExpiryDate(LocalDateTime passwordResetTokenExpiryDate) {
        this.passwordResetTokenExpiryDate = passwordResetTokenExpiryDate;
    }

    public void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        this.publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
    }

    public PrivateKey getPrivateKey() {
        if (this.privateKey == null) return null;
        byte[] privateKeyBytes = Base64.getDecoder().decode(this.privateKey);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Error reconstructing private key", e);
        }
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}