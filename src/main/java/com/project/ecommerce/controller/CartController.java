package com.project.ecommerce.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.model.User;
import com.project.ecommerce.dto.cart.CartResponseDto;
import com.project.ecommerce.service.CartServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CartController {
    
    private final CartServiceImpl cartServiceImpl;

    @GetMapping
    public ResponseEntity<CartResponseDto> getActiveCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok().body(cartServiceImpl.getActiveCart(user.getId()));
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartResponseDto> addToCart(Authentication auth, @PathVariable UUID productId, @RequestBody Map<String, Integer> body) {
        User user = (User) auth.getPrincipal();
        int quantity = body.getOrDefault("quantity", 1);
        CartResponseDto cart = cartServiceImpl.addToCart(user.getId(), productId, quantity);

        return ResponseEntity.ok().body(cart);
    }

    @PatchMapping("/increase/{productId}")
    public ResponseEntity<CartResponseDto> increaseQuantity(Authentication auth, @PathVariable UUID productId) {
        
        User user = (User) auth.getPrincipal();
        CartResponseDto cart = cartServiceImpl.increaseQuantity(user.getId(), productId);

        return ResponseEntity.ok().body(cart);
    }

    @PatchMapping("/decrease/{productId}")
    public ResponseEntity<CartResponseDto> decreaseQuantity(Authentication auth, @PathVariable UUID productId) {
        
        User user = (User) auth.getPrincipal();
        CartResponseDto cart = cartServiceImpl.decreaseQuantity(user.getId(), productId);

        return ResponseEntity.ok().body(cart);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponseDto> removeItem(Authentication auth, @PathVariable UUID productId) {
        
        User user = (User) auth.getPrincipal();
        CartResponseDto cart = cartServiceImpl.removeItem(user.getId(), productId);

        return ResponseEntity.ok().body(cart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartResponseDto> clearCart(Authentication auth) {
        
        User user = (User) auth.getPrincipal();
        CartResponseDto cart = cartServiceImpl.clearCart(user.getId());

        return ResponseEntity.ok().body(cart);
    }

}
