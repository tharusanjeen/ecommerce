package com.projectone.ecommerce.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.projectone.ecommerce.exception.CustomExceptions.EmailAlreadyExistsException;
import com.projectone.ecommerce.exception.CustomExceptions.UsernameAlreadyTakenException;
import com.projectone.ecommerce.exception.validation.ValidationError;
import com.projectone.ecommerce.exception.validation.ValidationResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException e,
            HttpServletRequest request) {
        ErrorResponse error = ErrorResponse
                .builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException e,
            HttpServletRequest request) {
        ErrorResponse error = ErrorResponse
                .builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e,
            HttpServletRequest request) {
        ErrorResponse error = ErrorResponse
                .builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        List<ValidationError> errors = e.getFieldErrors().stream()
                .map(err -> new ValidationError(err.getField(), err.getDefaultMessage())).collect(Collectors.toList());

        ValidationResponse response = new ValidationResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed!",
                request.getRequestURI(),
                LocalDateTime.now(),
                errors
                );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
