package com.project.ecommerce.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.ecommerce.dto.PagedResponse;
import com.project.ecommerce.dto.filter.ProductFilter;
import com.project.ecommerce.dto.product.ProductPatchDto;
import com.project.ecommerce.dto.product.ProductRequestDto;
import com.project.ecommerce.dto.product.ProductResponseDto;
import com.project.ecommerce.exception.CustomExceptions.CategoryNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.ProductNotFoundException;
import com.project.ecommerce.model.Category;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.repository.CategoryRepository;
import com.project.ecommerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Value("${app.base-url}")
    private String baseUrl;

@Override
public PagedResponse<ProductResponseDto> getProducts(ProductFilter productFilter, boolean isAdmin) {

    Sort sort = productFilter.getDirection().equalsIgnoreCase("asc")
            ? Sort.by(productFilter.getSortBy()).ascending()
            : Sort.by(productFilter.getSortBy()).descending();

    Pageable pageable = PageRequest.of(
            productFilter.getPage(),
            productFilter.getSize(),
            sort
    );

    String searchTerm = productFilter.getSearch();
    if (searchTerm != null && !searchTerm.isBlank()) {
        searchTerm = searchTerm.trim().toLowerCase();
    } else {
        searchTerm = null;
    }

    List<UUID> categoryIds = productFilter.getCategoryIds();
    if (categoryIds == null || categoryIds.isEmpty()) {
        categoryIds = null;
    }

    Page<Product> productPage = productRepository.findAllWithFilters(
            categoryIds,
            searchTerm,
            isAdmin,
            pageable
    );

    List<ProductResponseDto> content = productPage
            .map(product -> ProductResponseDto.from(product, baseUrl))
            .getContent();

    return new PagedResponse<>(
            content,
            productPage.getNumber(),
            productPage.getSize(),
            productPage.getTotalElements(),
            productPage.getTotalPages(),
            productPage.isLast()
    );
}

    @Override
    public ProductResponseDto getSingleProduct(UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return ProductResponseDto.from(product, baseUrl);
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {

        Category category = categoryRepository.findByIdAndIsActiveTrue(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        Product product = Product
                .builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .stockQuantity(requestDto.getStockQuantity())
                .discountPercentage(requestDto.getDiscountPercentage() != null ? requestDto.getDiscountPercentage()
                        : BigDecimal.ZERO)
                .category(category)
                .build();

        return ProductResponseDto.from(productRepository.save(product), baseUrl);
    }

    @Override
    public List<ProductResponseDto> createBulkProducts(List<ProductRequestDto> requestDto) {

        if (requestDto.isEmpty()) {
            throw new RuntimeException("Products list cannot be empty!");
        }

        Set<String> names = new HashSet<>();
        Map<String, Category> categoryMap = new HashMap<>();
        for (ProductRequestDto product : requestDto) {
            if (!names.add(product.getName().toLowerCase())) {
                throw new ProductNotFoundException("Duplicate product name in the list!");
            }

            Category category = categoryRepository.findByIdAndIsActiveTrue(product.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(
                            "Category not found with id : " + product.getCategoryId()));

            categoryMap.put(category.getId().toString(), category);
        }

        List<Product> products = requestDto.stream().map(product -> Product.builder().name(product.getName())
                .description(product.getDescription()).price(product.getPrice())
                .discountPercentage(
                        product.getDiscountPercentage() == null ? BigDecimal.ZERO : product.getDiscountPercentage())
                .stockQuantity(product.getStockQuantity()).category(categoryMap.get(product.getCategoryId().toString()))
                .build()).collect(Collectors.toList());

        return productRepository.saveAll(products).stream().map(product -> ProductResponseDto.from(product, baseUrl))
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto requestDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));

        Category category = categoryRepository.findByIdAndIsActiveTrue(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setStockQuantity(requestDto.getStockQuantity());
        product.setDiscountPercentage(
                requestDto.getDiscountPercentage() != null ? requestDto.getDiscountPercentage() : BigDecimal.ZERO);
        product.setCategory(category);
        product.setProductStatus(requestDto.getProductStatus());

        return ProductResponseDto.from(productRepository.save(product), baseUrl);
    }

    @Override
    public ProductResponseDto patchProduct(UUID id, ProductPatchDto patchDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));

        if (patchDto.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndIsActiveTrue(patchDto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

            product.setCategory(category);
        }

        if (patchDto.getName() != null) {
            product.setName(patchDto.getName());
        }

        if (patchDto.getDescription() != null) {
            product.setDescription(patchDto.getDescription());
        }

        if (patchDto.getStockQuantity() != null) {
            product.setStockQuantity(patchDto.getStockQuantity());
        }

        if (patchDto.getPrice() != null) {
            product.setPrice(patchDto.getPrice());
        }

        if (patchDto.getDiscountPercentage() != null) {
            product.setDiscountPercentage(patchDto.getDiscountPercentage());
        }

        if (patchDto.getProductStatus() != null) {
            product.setProductStatus(patchDto.getProductStatus());
        }

        return ProductResponseDto.from(productRepository.save(product), baseUrl);
    }

    @Override
    public Map<String, Object> deleteProduct(UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        productRepository.delete(product);
        return Map.of("success", true, "message", "Deleted product with id : " + product.getId());
    }

    @Override
    public ProductResponseDto getSingleProductByCategoryIsActive(UUID id) {

        Product product = productRepository.findByIdAndCategoryIsActiveTrue(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));

        return ProductResponseDto.from(product, baseUrl);
    }
}
