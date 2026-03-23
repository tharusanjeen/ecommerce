package com.project.ecommerce.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ecommerce.dto.category.CategoryResponseDto;
import com.project.ecommerce.dto.image.ProductImageResponse;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.model.Enums.ProductStatus;

public record ProductResponseDto(UUID id, String name, String description, BigDecimal price, Integer stockQuantity, BigDecimal discountPercentage,
        @JsonInclude(JsonInclude.Include.NON_NULL) BigDecimal discountedPrice, List<ProductImageResponse> images, CategoryResponseDto category, LocalDateTime created_at,
        LocalDateTime updated_at,
        ProductStatus productStatus) {

    public static ProductResponseDto from(Product product, String baseUrl) {
        BigDecimal discountedPrice = product.getDiscountPercentage() != null && product.getDiscountPercentage().compareTo(BigDecimal.ZERO) > 0
                ? product.getDiscountedPrice()
                : null;
        List<ProductImageResponse> images = product.getImages().stream().map(image -> ProductImageResponse.from(image, baseUrl)).collect(Collectors.toList());

        return new ProductResponseDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStockQuantity(),
                product.getDiscountPercentage(), discountedPrice, images, CategoryResponseDto.from(product.getCategory()),
                product.getCreatedAt(), product.getUpdatedAt(), product.getProductStatus());
    }

}
