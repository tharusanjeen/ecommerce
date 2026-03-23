package com.project.ecommerce.dto.image;

import java.util.UUID;

import com.project.ecommerce.model.ProductImage;

public record ProductImageResponse(UUID id, String filename, String url, boolean isPrimary, String altText) {

    public static ProductImageResponse from(ProductImage image, String baseUrl) {
        return new ProductImageResponse(image.getId(), image.getFilename(), image.getFullUrl(baseUrl), image.isPrimary(), image.getAltText());
    } 
}
