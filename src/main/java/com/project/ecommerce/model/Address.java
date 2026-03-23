package com.project.ecommerce.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String province;
    
    @Column(nullable = false)
    private String district;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String street;
    
    @Column(nullable = false)
    private boolean isDefault;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Column(nullable = false, updatable = true)
    private LocalDateTime updated_at;
}
