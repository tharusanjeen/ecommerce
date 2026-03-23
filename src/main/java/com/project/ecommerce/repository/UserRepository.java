package com.project.ecommerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByUsernameOrEmail(String username, String email);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
