package com.project.ecommerce.model;

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

    public enum CartStatus {
        ACTIVE,
        ORDERED,
        ABANDONED,
        EXPIRED
    }

    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        SHIPPED,
        OUT_FOR_DELIVERY,
        DELIVERED,
        COMPLETED,
        CANCELLED
    }

    public enum PaymentMethod {
        CASH_ON_DELIVERY,
        STRIPE
    }

    public enum PaymentStatus {
        UNPAID,
        PAID
    }
}
