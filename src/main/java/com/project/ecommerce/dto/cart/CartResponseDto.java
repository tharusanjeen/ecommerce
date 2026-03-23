package com.project.ecommerce.dto.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.project.ecommerce.model.Cart;
import com.project.ecommerce.model.Enums.CartStatus;

public record CartResponseDto(UUID id, UUID userId, List<CartItemResponseDto> items, CartStatus status, BigDecimal totalAmount) {
    
    public static CartResponseDto from(Cart cart) {
        return new CartResponseDto(cart.getId(), cart.getUser().getId(), cart.getItems().stream().map(CartItemResponseDto::from).collect(Collectors.toList()), cart.getStatus(), cart.getTotalAmount());
    }
}
