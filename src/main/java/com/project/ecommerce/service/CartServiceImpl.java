package com.project.ecommerce.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.ecommerce.dto.cart.CartResponseDto;
import com.project.ecommerce.exception.CustomExceptions.ItemNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.NotEnoughStockException;
import com.project.ecommerce.exception.CustomExceptions.ProductNotFoundException;
import com.project.ecommerce.model.Cart;
import com.project.ecommerce.model.CartItem;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.model.ProductImage;
import com.project.ecommerce.model.User;
import com.project.ecommerce.model.Enums.CartStatus;
import com.project.ecommerce.repository.CartRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public CartResponseDto getActiveCart(UUID userId) {

        User user = getUser(userId);

        return CartResponseDto.from(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .user(user)
                            .status(CartStatus.ACTIVE)
                            .build();

                    return cartRepository.save(cart);
                }));
    }

    @Override
    public CartResponseDto addToCart(UUID userId, UUID productId, int quantity) {

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        Cart cart = getCart(userId);

        Product product = getProduct(productId);

        CartItem existingItem = findItem(cart, productId);

        // Case A - Product already in cart -> increment quantity
        if (existingItem != null) {

            int newQuantity = existingItem.getQuantity() + quantity;

            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Not enough stock available!");
            }

            existingItem.setQuantity(newQuantity);
            cart.calculateTotalAmt();
            return CartResponseDto.from(cartRepository.save(cart));
        }

        // Case B - Product not in cart -> add new item with quantity = 1
        if (product.getStockQuantity() < quantity) {
            throw new NotEnoughStockException("Product is out of stock");
        }

        String primaryImage = product.getImages()
                .stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .map(img -> img.getFullUrl(baseUrl))
                .orElse(baseUrl + "/" + "default.png");

        CartItem newItem = CartItem.builder()
                .product(product)
                .name(product.getName())
                .cart(cart)
                .quantity(1)
                .priceAtAddition(product.getPrice())
                .discountAtAddition(product.getDiscountPercentage())
                .primaryImageUrl(primaryImage)
                .build();

        cart.addItem(newItem);
        cart.calculateTotalAmt();

        return CartResponseDto.from(cartRepository.save(cart));
    }

    @Override
    public CartResponseDto increaseQuantity(UUID userid, UUID productId) {

        Cart cart = getCart(userid);

        Product product = getProduct(productId);

        CartItem existingItem = findItemOrThrow(cart, productId);

        if (product.getStockQuantity() < 1) {
            throw new NotEnoughStockException("Not enough stock available!");
        }

        existingItem.setQuantity(existingItem.getQuantity() + 1);
        cart.calculateTotalAmt();

        return CartResponseDto.from(cartRepository.save(cart));

    }

    @Override
    public CartResponseDto decreaseQuantity(UUID userid, UUID productId) {

        Cart cart = getCart(userid);

        CartItem existingItem = findItemOrThrow(cart, productId);

        if (existingItem.getQuantity() < 0) {
            cart.removeItem(existingItem);
            cart.calculateTotalAmt();
            return CartResponseDto.from(cartRepository.save(cart));
        }

        existingItem.setQuantity(existingItem.getQuantity() - 1);
        cart.calculateTotalAmt();

        return CartResponseDto.from(cartRepository.save(cart));
    }

    @Override
    public CartResponseDto removeItem(UUID userid, UUID productId) {

        Cart cart = getCart(userid);

        CartItem existingItem = findItemOrThrow(cart, productId);

        cart.removeItem(existingItem);
        cart.calculateTotalAmt();

        return CartResponseDto.from(cartRepository.save(cart));
    }

    @Override
    public CartResponseDto clearCart(UUID userid) {

        Cart cart = getCart(userid);

        cart.getItems().clear();
        ;
        cart.calculateTotalAmt();

        return CartResponseDto.from(cartRepository.save(cart));
    }

    // Helper methods
    private User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    private Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    private Cart getCart(UUID userId) {
        User user = getUser(userId);

        return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseGet(() -> {

                    Cart newCart = Cart.builder()
                            .user(user)
                            .status(CartStatus.ACTIVE)
                            .totalAmount(BigDecimal.ZERO)
                            .build();

                    return cartRepository.save(newCart);
                });
    }

    private CartItem findItem(Cart cart, UUID productId) {

        return cart
                .getItems()
                .stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private CartItem findItemOrThrow(Cart cart, UUID productId) {
        return cart
                .getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Item not found!"));
    }

}
