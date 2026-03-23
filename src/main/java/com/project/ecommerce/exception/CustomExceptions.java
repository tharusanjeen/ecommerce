package com.project.ecommerce.exception;

public class CustomExceptions {

    public static class UsernameAlreadyTakenException extends RuntimeException {
        public UsernameAlreadyTakenException(String message) {
            super(message);
        }
    }

    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }   
    }

    public static class CategoryAlreadyExistsException extends RuntimeException {
        public CategoryAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class CategoryNotFoundException extends RuntimeException {
        public CategoryNotFoundException(String message) {
            super(message);
        }
    }

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public static class RequestEntityTooLargeException extends RuntimeException {
        public RequestEntityTooLargeException(String message) {
            super(message);
        }
    }

    public static class ImageNotFoundException extends RuntimeException {
        public ImageNotFoundException(String message) {
            super(message);
        }
    }
    
    public static class CartNotFoundException extends RuntimeException {
        public CartNotFoundException(String message) {
            super(message);
        }
    }

    public static class ItemNotFoundException extends RuntimeException {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }

    public static class NotEnoughStockException extends RuntimeException {
        public NotEnoughStockException(String message) {
            super(message);
        }
    }

    public static class AddressNotFoundException extends RuntimeException {
        public AddressNotFoundException(String message) {
            super(message);
        }
    }
}