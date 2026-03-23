package com.project.ecommerce.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.project.ecommerce.dto.PagedResponse;
import com.project.ecommerce.dto.filter.ProductFilter;
import com.project.ecommerce.dto.product.ProductPatchDto;
import com.project.ecommerce.dto.product.ProductRequestDto;
import com.project.ecommerce.dto.product.ProductResponseDto;

public interface ProductService {

    /**
     * Get all products.
     * 
     * @return the List of ProductResponseDto containing the details of Products.
     */
    PagedResponse<ProductResponseDto> getProducts(ProductFilter productFilter, boolean isAdmin);

    /**
     * Get single product by id.
     * 
     * @param id the UUID of the requested product.
     * @return the ProductResponseDto containing the details of that product.
     */
    ProductResponseDto getSingleProduct(UUID id);

    /**
     * Create new product.
     * 
     * @param requestDto the ProductRequestDto containing new product details.
     * @return the ProductResponseDto of the new created Product.
     */
    ProductResponseDto createProduct(ProductRequestDto requestDto);

    /**
     * Create new products.
     * 
     * @param requestDto the Lis tof  ProductRequestDto's containing new product details.
     * @return the List of ProductResponseDto of the new created Products.
     */
    List<ProductResponseDto> createBulkProducts(List<ProductRequestDto> requestDto);

    /**
     * Update a product by id.
     * 
     * @param id the UUID of the requested product.
     * @param requestDto the ProductRequestDto containing new product details.
     * @return ProductResponseDto of the updated product.
     */
    ProductResponseDto updateProduct(UUID id, ProductRequestDto requestDto);

    /**
     * Patch specific field of a product.
     * 
     * @param id the UUID of the requested product.
     * @param patchDto the ProductPatchDto containing new field.
     * @return ProductResponseDto of the patched product.
     */
    ProductResponseDto patchProduct(UUID id, ProductPatchDto patchDto);

    /**
     * Delete a product by id.
     * 
     * @param id the UUID of the product.
     * @return the Map containing the message 'deleted' and the product.
     */
    Map<String, Object> deleteProduct(UUID id);

    // For Users
    /**
     * Get a single product by id.
     * 
     * @param id the UUID of requested product.
     * @return the ProductResponseDto containg the details of product.
     */
    ProductResponseDto getSingleProductByCategoryIsActive(UUID id);
}