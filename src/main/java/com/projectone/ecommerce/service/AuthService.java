package com.projectone.ecommerce.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectone.ecommerce.dtos.UserDto;
import com.projectone.ecommerce.dtos.auth.AuthRequest;
import com.projectone.ecommerce.dtos.auth.AuthResponse;
import com.projectone.ecommerce.dtos.auth.RegisterRequest;
import com.projectone.ecommerce.exception.CustomExceptions.*;
import com.projectone.ecommerce.model.User;
import com.projectone.ecommerce.model.Enums;
import com.projectone.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameAlreadyTakenException("Username is taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        User user = User
                .builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Enums.Role.USER) // Default role
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserDto.from(user))
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));

        User user = userRepository.findUserByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserDto.from(user))
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (refreshToken.isBlank() || refreshToken == null) {
            throw new IllegalArgumentException("Refresh token is missing!");
        }

        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findUserByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Refresh token expired or invalid!");
        }

        String newAccessToken = jwtService.generateAccessToken(user);

        return AuthResponse
                .builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .user(UserDto.from(user))
                .build();
    }
}