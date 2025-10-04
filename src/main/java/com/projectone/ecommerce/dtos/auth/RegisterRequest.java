package com.projectone.ecommerce.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    
    @NotBlank(message = "Username is required!")
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, max = 15, message = "Password must be 8 characters")
    private String password;
}
