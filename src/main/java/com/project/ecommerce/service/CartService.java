package com.project.ecommerce.service;

import java.util.UUID;

import com.project.ecommerce.dto.cart.CartResponseDto;

public interface CartService {
    
    /**
     * Get active Cart for user
     * 
     * @param userId UUID of user
     * @return the active Cart of user.
     */
    CartResponseDto getActiveCart(UUID userId);

    /**
     * Add product to cart
     * 
     * @param userId id of user.
     * @param productId id of product to be added to cart.
     * @return the Cart
     */
    CartResponseDto addToCart(UUID userId, UUID productId, int quantity);
    
    /**
     * Increase quantity of Product
     * 
     * @param userId id of user
     * @param productId id of product of which quantity will be increased.
     * @return the latest Cart.
     */
    CartResponseDto increaseQuantity(UUID userId, UUID productId);
    

    /**
     * Decrease the quantity of Product
     * 
     * @param userId id of user
     * @param productId id of product of which quantity will be decreased.
     * @return the latest Cart.
     */
    CartResponseDto decreaseQuantity(UUID userId, UUID productId);
    

    /**
     * Remove the product from cart
     * 
     * @param userId id of user
     * @param productId id of Product to be removed.
     * @return the latest Cart
     */
    CartResponseDto removeItem(UUID userId, UUID productId);
    

    /**
     * Remove all products from cart
     * 
     * @param userId the id of user
     */
    CartResponseDto clearCart(UUID userId);
}
