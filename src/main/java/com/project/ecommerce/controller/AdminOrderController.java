package com.project.ecommerce.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.order.OrderDetailsDto;
import com.project.ecommerce.dto.order.UpdateOrderStatusRequest;
import com.project.ecommerce.service.OrderServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {
    
    private final OrderServiceImpl orderServiceImpl;

    @GetMapping
    public ResponseEntity<List<OrderDetailsDto>> getAllOrderes() {

        return ResponseEntity.ok().body(orderServiceImpl.getAllOrders());
    }

    @PostMapping("/{orderId}/update_status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable UUID orderId, @RequestBody UpdateOrderStatusRequest request) {
        orderServiceImpl.updateOrderStatus(orderId, request.getStatus());

        return ResponseEntity.ok().body("Order status updated.");
    }
}
