package com.project.ecommerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

    List<ProductImage> findByProductId(UUID id);
    Optional<ProductImage> findByIdAndProductId(UUID imageId, UUID productId);
    Optional<ProductImage> findFirstByProductId(UUID productId);

    int countByProductId(UUID id);

    boolean existsByProductIdAndIsPrimaryTrue(UUID id);

}
