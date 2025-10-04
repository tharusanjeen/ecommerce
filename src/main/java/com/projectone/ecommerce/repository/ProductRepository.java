package com.projectone.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectone.ecommerce.model.Enums;
import com.projectone.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryId(UUID categoryId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByStatus(Enums.ProductStatus status);

}
