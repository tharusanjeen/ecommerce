package com.project.ecommerce.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ecommerce.dto.UserDto;
import com.project.ecommerce.dto.auth.AuthRequest;
import com.project.ecommerce.dto.auth.AuthResponse;
import com.project.ecommerce.dto.auth.AuthResult;
import com.project.ecommerce.dto.auth.RegisterRequest;
import com.project.ecommerce.exception.CustomExceptions.EmailAlreadyExistsException;
import com.project.ecommerce.exception.CustomExceptions.UsernameAlreadyTakenException;
import com.project.ecommerce.model.User;
import com.project.ecommerce.model.Enums.Role;
import com.project.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResult register(RegisterRequest request) {

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyTakenException("Username is taken!");
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        User user = User
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String accessToken = jwtServiceImpl.generateAccessToken(user);
        String refreshToken = jwtServiceImpl.generateRefreshToken(user);

        return new AuthResult(accessToken, refreshToken, UserDto.from(user));
    }

    public AuthResult authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsernameOrEmail(), authRequest.getPassword())
        );

        User user = userRepository.findUserByUsernameOrEmail(authRequest.getUsernameOrEmail(), authRequest.getUsernameOrEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        String accessToken = jwtServiceImpl.generateAccessToken(user);
        String refreshToken = jwtServiceImpl.generateRefreshToken(user);

        return new AuthResult(accessToken, refreshToken, UserDto.from(user));
    }

    public AuthResponse refresh(String refreshToken) {
        if(refreshToken.isBlank() || refreshToken == null) {
            throw new RuntimeException("Refresh token missing!");
        }

        String username = jwtServiceImpl.extractUsername(refreshToken);

        User user = userRepository.findUserByUsernameOrEmail(username, username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if(!jwtServiceImpl.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Refresh token expired or invalid");
        }

        String accessToken = jwtServiceImpl.generateAccessToken(user);

        return AuthResponse.builder().accessToken(accessToken).user(UserDto.from(user)).build();
    }
}
