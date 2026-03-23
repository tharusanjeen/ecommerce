package com.project.ecommerce.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.order.OrderDetailsDto;
import com.project.ecommerce.dto.order.OrderItemResponseDto;
import com.project.ecommerce.dto.order.OrderRequestDto;
import com.project.ecommerce.dto.order.OrderResponseDto;
import com.project.ecommerce.model.User;
import com.project.ecommerce.service.OrderServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody OrderRequestDto orderRequestDto,
            Authentication auth) {
        User user = (User) auth.getPrincipal();

        OrderResponseDto response = orderServiceImpl.placeOrder(orderRequestDto, user.getId());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/myorders")
    public ResponseEntity<List<OrderDetailsDto>> getMyOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<OrderDetailsDto> orders = orderServiceImpl.getMyOrders(user.getId());
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/order_details/{orderId}")
    public ResponseEntity<List<OrderItemResponseDto>> getMyOrderDetails(Authentication auth, @PathVariable UUID orderId) {
        User user = (User) auth.getPrincipal();

        List<OrderItemResponseDto> orderItems = orderServiceImpl.getOrderDetails(user.getId(), orderId);

        return ResponseEntity.ok().body(orderItems);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable UUID orderId, Authentication auth) {
        User user = (User) auth.getPrincipal();

        orderServiceImpl.cancelOrder(orderId, user.getId());
        return ResponseEntity.ok().body("Order cancelled successfully!");
    }
}
