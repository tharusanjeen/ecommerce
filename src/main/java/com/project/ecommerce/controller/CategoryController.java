package com.project.ecommerce.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.category.CategoryResponseDto;
import com.project.ecommerce.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryResponseDto>> getAllActiveCategories() {
        List<CategoryResponseDto> response = categoryService.getAllActiveCategories();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable UUID id) {
        CategoryResponseDto responseDto = categoryService.getSingleActiveCategory(id);

        return ResponseEntity.ok().body(responseDto);
    }

}
