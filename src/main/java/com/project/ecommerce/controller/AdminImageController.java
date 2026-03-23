package com.project.ecommerce.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.image.ProductImageRequest;
import com.project.ecommerce.dto.image.ProductImageResponse;
import com.project.ecommerce.service.image.ProductImageServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products/{productId}/images")
@PreAuthorize("hasRole('ADMIN')")
public class AdminImageController {
    
    private final ProductImageServiceImpl imageServiceImpl;

    @PostMapping
    public ResponseEntity<List<ProductImageResponse>> uploadImages(@PathVariable UUID productId, @ModelAttribute ProductImageRequest request) {
        List<ProductImageResponse> response = imageServiceImpl.uploadImages(productId, request);

        return ResponseEntity.ok().body(response);
    }

    // @GetMapping
    // public ResponseEntity<List<ProductImageResponse>> getImagesOfProduct(@PathVariable UUID productId) {
    //     List<ProductImageResponse> images = imageServiceImpl.getImagesByProductId(productId);

    //     return ResponseEntity.ok().body(images);
    // }

    @PatchMapping("/{id}/primary")
    public ResponseEntity<List<ProductImageResponse>> updatePrimaryImage(@PathVariable UUID productId, @PathVariable UUID id) {
        List<ProductImageResponse> images = imageServiceImpl.updatePrimaryImage(productId, id);

        return ResponseEntity.ok().body(images);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteImageById(@PathVariable UUID productId, @PathVariable UUID id) {
        return ResponseEntity.ok().body(imageServiceImpl.deleteImageById(productId, id));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteAllImagesByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok().body(imageServiceImpl.deleteAllImagesByProductId(productId));
    }

}
