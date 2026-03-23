package com.project.ecommerce.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username is required!")
    @Size(min = 4, max = 15, message = "Username must be between 4 to 15 characters long")
    @Pattern(
        regexp = "^[a-zA-Z][a-zA-Z0-9._]{3,14}$",
        message = "Invalid username!"
    )
    private String username;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, message = "Password cannot be less than 8 characters")
    private String password;
}
