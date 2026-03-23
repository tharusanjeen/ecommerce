package com.project.ecommerce.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.auth.AuthRequest;
import com.project.ecommerce.dto.auth.AuthResponse;
import com.project.ecommerce.dto.auth.AuthResult;
import com.project.ecommerce.dto.auth.RegisterRequest;
import com.project.ecommerce.service.AuthServiceImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthServiceImpl authServiceImpl;

    @Value("${jwt.refreshTokenExpiry}")
    private long refreshTokenExpiry;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
        @Valid @RequestBody RegisterRequest request,
        HttpServletResponse response
    ) {

        AuthResult authResult = authServiceImpl.register(request);

        ResponseCookie refreshCookie = ResponseCookie
        .from("refreshToken", authResult.getRefreshToken())
        .httpOnly(true)
        .secure(false)
        .path("/")
        .maxAge(refreshTokenExpiry / 1000)
        .sameSite("Lax")
        .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(
            AuthResponse.builder()
            .accessToken(authResult.getAccessToken())
            .user(authResult.getUser())
            .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @Valid @RequestBody AuthRequest request,
        HttpServletResponse response
    ) {
        AuthResult authResult = authServiceImpl.authenticate(request);

        ResponseCookie refreshCookie = ResponseCookie
        .from("refreshToken", authResult.getRefreshToken())
        .httpOnly(true)
        .secure(false)
        .path("/")
        .maxAge(refreshTokenExpiry / 1000)
        .sameSite("Lax")
        .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(
            new AuthResponse(authResult.getAccessToken(), authResult.getUser())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
        @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        AuthResponse response = authServiceImpl.refresh(refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();

    }
}
