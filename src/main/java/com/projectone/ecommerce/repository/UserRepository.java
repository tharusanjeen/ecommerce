package com.projectone.ecommerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectone.ecommerce.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
