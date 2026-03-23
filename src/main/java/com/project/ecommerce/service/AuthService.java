package com.project.ecommerce.service;

import com.project.ecommerce.dto.auth.AuthRequest;
import com.project.ecommerce.dto.auth.AuthResponse;
import com.project.ecommerce.dto.auth.AuthResult;
import com.project.ecommerce.dto.auth.RegisterRequest;

public interface AuthService {


    /**
     * Register a new user.
     * 
     * @param request the RegisterRequuest containing user details.
     * @return AuthResponse containing access and refresh tokens and user details.
     */
    AuthResult register(RegisterRequest request);


    /**
     * Authenticate a user and generate JWT tokens.
     * 
     * @param request the AuthRequest containing username or email and password
     * @return AuthResponse containing access and refresh tokens and user details
     */
    AuthResult authenticate(AuthRequest request);
    
    /**
     * Refresh access token for a user.
     * 
     * @param refreshToken the String refresh token
     * @return  a Map containing the new access token and possibly other related information
     */
    AuthResponse refresh(String refreshToken);
}