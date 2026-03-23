package com.project.ecommerce.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.project.ecommerce.dto.category.CategoryPatchDto;
import com.project.ecommerce.dto.category.CategoryRequestDto;
import com.project.ecommerce.dto.category.CategoryResponseDto;
import com.project.ecommerce.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminCategoryController {
    
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> response = categoryService.getAllCategories();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getSingleCategory(@PathVariable UUID id) {
        CategoryResponseDto response = categoryService.getSingleCategory(id);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto response = categoryService.createCategory(requestDto);
        
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/bulk/create")
    public ResponseEntity<List<CategoryResponseDto>> createBulkCategories(@Valid @RequestBody List<CategoryRequestDto> requestDto) {
        List<CategoryResponseDto> response = categoryService.createBulkCategories(requestDto);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@Valid @RequestBody CategoryRequestDto requestDto, @PathVariable UUID id) {
        CategoryResponseDto response = categoryService.updateCategory(id, requestDto);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<Map<String, Object>> patchCategory(@PathVariable UUID id, @Valid @RequestBody CategoryPatchDto patchDto) {
        return ResponseEntity.ok().body(categoryService.patchCategory(id, patchDto));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<CategoryResponseDto> toggleCategory(@PathVariable UUID id) {
        return ResponseEntity.ok().body(categoryService.activeToggle(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable UUID id) {
        Map<String, Object> response = categoryService.deleteCategory(id);

        return ResponseEntity.ok().body(response);
    } 

}
