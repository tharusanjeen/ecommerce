package com.projectone.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectone.ecommerce.dtos.auth.AuthRequest;
import com.projectone.ecommerce.dtos.auth.AuthResponse;
import com.projectone.ecommerce.dtos.auth.RefreshToken;
import com.projectone.ecommerce.dtos.auth.RegisterRequest;
import com.projectone.ecommerce.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshToken request) {
        String token = request.getRefreshToken();

        AuthResponse response = authService.refreshToken(token);
        return ResponseEntity.ok(response);

    }
}
