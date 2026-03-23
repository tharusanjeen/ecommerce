package com.project.ecommerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.model.Order;
import com.project.ecommerce.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
    Optional<Order> findByIdAndUserId(UUID orderId, UUID userId);
}
