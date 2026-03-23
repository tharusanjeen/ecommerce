package com.project.ecommerce.dto.filter;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductFilter {
    private List<UUID> categoryIds;
    private String search;
    private String sortBy = "createdAt";
    private String direction = "desc";

    @Min(0)
    private int page = 0;

    @Positive
    private int size = 20;
}
