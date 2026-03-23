package com.project.ecommerce.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.PagedResponse;
import com.project.ecommerce.dto.filter.ProductFilter;
import com.project.ecommerce.dto.product.ProductPatchDto;
import com.project.ecommerce.dto.product.ProductRequestDto;
import com.project.ecommerce.dto.product.ProductResponseDto;
import com.project.ecommerce.service.ProductServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {
    
    private final ProductServiceImpl productServiceiImpl;

    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponseDto>> getAllProducts(@Valid ProductFilter productFilter) {

    
        return ResponseEntity.ok().body(productServiceiImpl.getProducts(productFilter, true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getSingleProducts(@PathVariable UUID id) {
        return ResponseEntity.ok().body(productServiceiImpl.getSingleProduct(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {

        ProductResponseDto response = productServiceiImpl.createProduct(requestDto);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/bulk/create")
    public ResponseEntity<List<ProductResponseDto>> createBulkProducts(@Valid @RequestBody List<ProductRequestDto> requestDto) {

        List<ProductResponseDto> response = productServiceiImpl.createBulkProducts(requestDto);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto response = productServiceiImpl.updateProduct(id, productRequestDto);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<ProductResponseDto> patchProduct(@PathVariable UUID id, @Valid @RequestBody ProductPatchDto patchDto) {
        ProductResponseDto response = productServiceiImpl.patchProduct(id, patchDto);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable UUID id) {
        return ResponseEntity.ok().body(productServiceiImpl.deleteProduct(id));
    }
}
