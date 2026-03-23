package com.project.ecommerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findCategoryByName(String categoryName);
    List<Category> findAllByIsActiveTrue();
    Optional<Category> findByIdAndIsActiveTrue(UUID id);

    boolean existsByName(String categoryName);
    
}
