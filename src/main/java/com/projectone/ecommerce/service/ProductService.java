package com.projectone.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.projectone.ecommerce.model.Product;
import com.projectone.ecommerce.repository.CategoryRepository;
import com.projectone.ecommerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    public Product createProduct(Product product) {
        if(product.getImages() != null && product.getImages().size() > 3) {
            product.setImages(product.getImages().subList(0, 3));
        }

        if(product.getCategory() != null) {
            UUID categoryUuid = product.getCategory().getId();
            categoryRepository.findById(categoryUuid)
            .orElseThrow(() -> new RuntimeException("Category not found!"));
        }

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found!"));
    }

    public List<Product> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Product not found!"));

        productRepository.delete(product);
    }
}