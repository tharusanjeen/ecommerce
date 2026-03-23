package com.project.ecommerce.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

import com.project.ecommerce.model.Enums.ProductStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 to 100 letters.")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 60, max = 300, message = "Description must be between 60 to 300 letters.")
    private String description;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 1, message = "Stock quantity cannot be less than 1")
    private Integer stockQuantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price cannot be negative")
    private BigDecimal price;

    @DecimalMax(value = "100.0", inclusive = true, message = "Discount percentage cannot exceed 100%")
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount percentage must be 0% or higher")
    private BigDecimal discountPercentage;

    @NotNull(message = "Category id is required")
    private UUID categoryId;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

}
