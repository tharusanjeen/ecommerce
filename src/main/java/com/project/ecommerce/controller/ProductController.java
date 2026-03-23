package com.project.ecommerce.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.PagedResponse;
import com.project.ecommerce.dto.filter.ProductFilter;
import com.project.ecommerce.dto.product.ProductResponseDto;
import com.project.ecommerce.service.ProductServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponseDto>> getAllProducts(
            @Valid ProductFilter productFilter) {

        return ResponseEntity.ok().body(productServiceImpl.getProducts(productFilter, false));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getSingleProducts(@PathVariable UUID id) {
        ProductResponseDto responseDto = productServiceImpl.getSingleProductByCategoryIsActive(id);
        return ResponseEntity.ok().body(responseDto);
    }

}
