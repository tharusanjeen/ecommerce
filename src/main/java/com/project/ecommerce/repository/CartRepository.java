package com.project.ecommerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.model.Cart;
import com.project.ecommerce.model.User;
import com.project.ecommerce.model.Enums.CartStatus;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserAndStatus(User user, CartStatus status);
}
