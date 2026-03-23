package com.project.ecommerce.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.ecommerce.dto.category.CategoryPatchDto;
import com.project.ecommerce.dto.category.CategoryRequestDto;
import com.project.ecommerce.dto.category.CategoryResponseDto;
import com.project.ecommerce.exception.CustomExceptions.CategoryAlreadyExistsException;
import com.project.ecommerce.exception.CustomExceptions.CategoryNotFoundException;
import com.project.ecommerce.model.Category;
import com.project.ecommerce.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        if (categoryRepository.existsByName(requestDto.getName())) {
            throw new CategoryAlreadyExistsException("Cateogry already exists!");
        }

        Category category = Category.builder().name(requestDto.getName()).description(requestDto.getDescription())
                .build();

        return CategoryResponseDto.from(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponseDto> createBulkCategories(List<CategoryRequestDto> requestDto) {

        if (requestDto.isEmpty()) {
            throw new RuntimeException("Category list cannot be empty!");
        }

        Set<String> names = new HashSet<>();
        for (CategoryRequestDto category : requestDto) {
            if (!names.add(category.getName().toLowerCase())) {
                throw new CategoryAlreadyExistsException("Duplicate category name in request!");
            }

            if (categoryRepository.existsByName(category.getName())) {
                throw new CategoryAlreadyExistsException("Category already exists! : " + category.getName());
            }
        }

        List<Category> categories = requestDto.stream().map(category -> Category.builder()
                .name(category.getName())
                .description(category.getDescription())
                .build()).collect(Collectors.toList());

        return categoryRepository.saveAll(categories).stream().map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<CategoryResponseDto> categoryResponseDtos = categoryRepository.findAll().stream()
                .map(CategoryResponseDto::from).collect(Collectors.toList());

        return categoryResponseDtos;
    }

    @Override
    public CategoryResponseDto getSingleCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        return CategoryResponseDto.from(category);
    }

    @Override
    public CategoryResponseDto updateCategory(UUID id, CategoryRequestDto requestDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        if (categoryRepository.existsByName(requestDto.getName())) {
            throw new CategoryAlreadyExistsException("Category already exists!");
        }

        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());

        return CategoryResponseDto.from(categoryRepository.save(category));
    }

    @Override
    public Map<String, Object> patchCategory(UUID id, CategoryPatchDto patchDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        if (patchDto.getName() != null) {
            category.setName(patchDto.getName());
        }

        if (patchDto.getDescription() != null) {
            category.setDescription(patchDto.getDescription());
        }

        if (patchDto.getIsActive() != null) {
            category.setActive(patchDto.getIsActive());
        }

        return Map.of("updated", "true", "category", CategoryResponseDto.from(categoryRepository.save(category)));
    }

    @Override
    public CategoryResponseDto activeToggle(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        category.setActive(!category.isActive());

        return CategoryResponseDto.from(categoryRepository.save(category));
    }

    public Map<String, Object> deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        categoryRepository.delete(category);

        return Map.of("success", true, "message", "Category deleted with id : " + category.getId());
    }

    // Active Categories
    @Override
    public List<CategoryResponseDto> getAllActiveCategories() {
        return categoryRepository.findAllByIsActiveTrue()
                .stream()
                .map(CategoryResponseDto::from).collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getSingleActiveCategory(UUID id) {
        Category category = categoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new CategoryNotFoundException("Active category not found!"));

        return CategoryResponseDto.from(category);
    }
}
