package com.project.ecommerce.dto.category;

import java.time.LocalDateTime;
import java.util.UUID;

import com.project.ecommerce.model.Category;

public record CategoryResponseDto(UUID id, String name, String description, boolean isActive, LocalDateTime created_at, LocalDateTime updated_at) {

    public static CategoryResponseDto from(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getDescription(), category.isActive(), category.getCreated_at(), category.getUpdated_at());
    }
}
