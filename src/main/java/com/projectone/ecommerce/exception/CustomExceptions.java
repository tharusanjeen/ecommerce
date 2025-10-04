package com.projectone.ecommerce.exception;

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
}
