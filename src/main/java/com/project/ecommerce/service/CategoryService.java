package com.project.ecommerce.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.project.ecommerce.dto.category.CategoryPatchDto;
import com.project.ecommerce.dto.category.CategoryRequestDto;
import com.project.ecommerce.dto.category.CategoryResponseDto;

public interface CategoryService {

    /**
     * Create a new Category.
     * 
     * @param requestDto the CategorRequestDto containing category details.
     * @return  CategoryResponseDto of the created category.
     */
    CategoryResponseDto createCategory(CategoryRequestDto requestDto);

    /**
     * Create a new Category.
     * 
     * @param requestDto the CategorRequestDto containing category details.
     * @return  CategoryResponseDto of the created category.
     */
    List<CategoryResponseDto> createBulkCategories(List<CategoryRequestDto> requestDto);

    /**
     * Get all categories.
     * 
     * @return List of CategoryResponseDto.
     */
    List<CategoryResponseDto> getAllCategories();

    /**
     * Get single category by id.
     * 
     * @param id UUID of the requested category.
     * @return CategoryResponseDto of the requested category.
     */
    CategoryResponseDto getSingleCategory(UUID id);

    /**
     * Update a category
     * 
     * @param id the UUID of the requested category.
     * @param requestDto the CategoryRequestDto containing new category details.
     * @return the CategoryResponseDto of new updated category.
     */
    CategoryResponseDto updateCategory(UUID id, CategoryRequestDto requestDto);


    /**
     * Patch any specific field of a Category.
     * 
     * @param id UUID of the requested Category.
     * @param patchDto the CategoryPatchDto containing new values.
     * @return the Map containing the message updated true and the new category.
     */
    Map<String, Object> patchCategory(UUID id, CategoryPatchDto patchDto);

    /**
     * Toggle active/inactive field of a Category.
     * 
     * @param id the UUID of the requested Category.
     * @return the CategoryResponseDto containing category details after isActive is toggled.
     */
    CategoryResponseDto activeToggle(UUID id);

    /**
     * Delete a category by id.
     * 
     * @param id the UUID of the requested category.
     * @return the Map containg the message 'deleted' and deleted category.
     */
    Map<String, Object> deleteCategory(UUID id);

    // For Users
    /**
     * Get all active categories.
     * 
     * @return the List of CategoryResponseDto containing all active categories details.
     */
    List<CategoryResponseDto> getAllActiveCategories();

    /**
     * Get a single active category by id. 
     * 
     * @param id the UUID of the category.
     * @return the CategoryResponseDto containg the details of that category.
     */
    CategoryResponseDto getSingleActiveCategory(UUID id);
}
