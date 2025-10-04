package com.projectone.ecommerce.model;

public class Enums {
    
    public enum Role {
        USER,
        ADMIN
    }

    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }

    public enum ProductStatus {
        AVAILABLE,
        OUT_OF_STOCK,
        DISCONTINUED
    }

    public enum OrderStatus {
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELED
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED
    }

    public enum CategoryStatus {
        ACTIVE,
        INACTIVE,
        HIDDEN
    }
}
