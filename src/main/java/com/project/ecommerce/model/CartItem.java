package com.project.ecommerce.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

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
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
   
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    private BigDecimal priceAtAddition;

    private BigDecimal discountAtAddition;

    @Column(name = "primary_image_url", nullable = false)
    private String primaryImageUrl;

    public BigDecimal getDiscountedPrice() {
        if (discountAtAddition == null || discountAtAddition.compareTo(BigDecimal.ZERO) <= 0) {
            return priceAtAddition;
        }

        BigDecimal discountedPrice = priceAtAddition.multiply(discountAtAddition)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return priceAtAddition.subtract(discountedPrice).setScale(2, RoundingMode.HALF_UP);
    }
}
