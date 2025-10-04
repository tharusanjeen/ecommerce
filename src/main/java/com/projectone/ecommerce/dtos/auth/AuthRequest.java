package com.projectone.ecommerce.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "Username or Email is required!")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required!")
    private String password;
}
