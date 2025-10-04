package com.projectone.ecommerce.exception.validation;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponse {
    private int statusCode;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private List<ValidationError> errors;
}
