package com.project.ecommerce.dto.auth;

import com.project.ecommerce.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResult {
    private String accessToken;
    private String refreshToken;
    private UserDto user;
}
