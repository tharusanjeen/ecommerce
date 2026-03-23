package com.project.ecommerce.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.project.ecommerce.config.ApplicationContextHolder;
import com.project.ecommerce.service.FileStorageServiceImpl;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 255)
    private String filename;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false)
    private boolean isPrimary;

    @Column(length = 255)
    private String altText;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    public String getFullUrl(String baseUrl) {
        return baseUrl + "/" + filePath;
    }

    @PreRemove
    public void preRemove() {
        FileStorageServiceImpl fileStorageServiceImpl = ApplicationContextHolder.getBean(FileStorageServiceImpl.class);
        fileStorageServiceImpl.deleteFile(filePath);
    }
}
