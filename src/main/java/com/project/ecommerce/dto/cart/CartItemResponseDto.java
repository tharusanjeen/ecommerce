package com.project.ecommerce.dto.cart;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ecommerce.model.CartItem;

public record CartItemResponseDto(UUID id, UUID cartId, UUID productId, String name, int quantity, BigDecimal priceAtAddition, BigDecimal discountAtAddition, String primaryImageUrl, @JsonInclude(JsonInclude.Include.NON_NULL) BigDecimal discountedPrice) {
    
    public static CartItemResponseDto from(CartItem item) {

        BigDecimal discountedPrice = item.getDiscountAtAddition() != null && item.getDiscountAtAddition().compareTo(BigDecimal.ZERO) > 0 ? item.getDiscountedPrice() : null;

        return new CartItemResponseDto(item.getId(), item.getCart().getId(), item.getProduct().getId(), item.getName(), item.getQuantity(), item.getPriceAtAddition(), item.getDiscountAtAddition(), item.getPrimaryImageUrl(), discountedPrice);
    }
}
