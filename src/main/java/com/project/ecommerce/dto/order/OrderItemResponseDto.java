package com.project.ecommerce.dto.order;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemResponseDto {
    
    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private BigDecimal discountPercentage;
}
