package com.authen.sec.repository;

import com.authen.sec.model.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByEmailConfirmationToken(String token);
    User findByPasswordResetToken(String token);
}