package com.project.ecommerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

  Optional<Product> findByIdAndCategoryIsActiveTrue(UUID id);

  boolean existsById(@NonNull UUID productId);

@Query("""
    SELECT p FROM Product p
    LEFT JOIN p.category c
    WHERE (:isAdmin = true OR c.isActive = true)
      AND (:categoryIds IS NULL OR c.id IN :categoryIds)
      AND (
            :search IS NULL
            OR :search = ''
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
          )
""")
Page<Product> findAllWithFilters(
        @Param("categoryIds") List<UUID> categoryIds,
        @Param("search") String search,
        @Param("isAdmin") boolean isAdmin,
        Pageable pageable);
}