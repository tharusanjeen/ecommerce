package com.project.ecommerce.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.project.ecommerce.exception.CustomExceptions.AddressNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.CartNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.CategoryAlreadyExistsException;
import com.project.ecommerce.exception.CustomExceptions.CategoryNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.EmailAlreadyExistsException;
import com.project.ecommerce.exception.CustomExceptions.ItemNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.NotEnoughStockException;
import com.project.ecommerce.exception.CustomExceptions.ProductNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.RequestEntityTooLargeException;
import com.project.ecommerce.exception.CustomExceptions.UsernameAlreadyTakenException;
import com.project.ecommerce.exception.validation.ValidationError;
import com.project.ecommerce.exception.validation.ValidationResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFoundException(CartNotFoundException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(ItemNotFoundException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAddressNotFoundException(AddressNotFoundException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughStockException(NotEnoughStockException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.CONFLICT, request);
    }

    @SuppressWarnings("null")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUUID(MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        // Check if the parameter was supposed to be a UUID
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(UUID.class)) {

            ErrorResponse response = ErrorResponse.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Invalid ID format")
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // fallback for other type mismatch errors
        ErrorResponse response = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Invalid parameter type.")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return buildError(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RequestEntityTooLargeException.class)
    public ResponseEntity<ErrorResponse> handleRequestEntityTooLargeException(RequestEntityTooLargeException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex,
            HttpServletRequest request) {

        return buildError(ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<ValidationError> errors = ex.getFieldErrors().stream()
                .map(err -> new ValidationError(err.getField(), err.getDefaultMessage())).collect(Collectors.toList());
        ValidationResponse response = new ValidationResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed!",
                request.getRequestURI(),
                LocalDateTime.now(),
                errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildError(Exception ex, HttpStatus httpStatus, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse
                .builder()
                .statusCode(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }
}