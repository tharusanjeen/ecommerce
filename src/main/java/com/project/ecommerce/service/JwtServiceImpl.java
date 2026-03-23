package com.project.ecommerce.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.project.ecommerce.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessTokenExpiry}")
    private long accessTokenExpiry;

    @Value("${jwt.refreshTokenExpiry}")
    private long refreshTokenExpiry;

    // Generate Secret Key
    private SecretKey getSigningKey() {
        byte[] keybytes = secret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keybytes);
    }

    // Extract all claims
    @Override
    public Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract one claim
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractClaims(token);

        return claimsResolver.apply(claims);
    }

    // Extract username
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check if token is expired
    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Check if token is valid
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Generate token
    @Override
    public String generateToken(Map<String, Object> extraClaims, String subject, long expiration) {
        return Jwts
                .builder()
                .subject(subject)
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Generate access token
    @Override
    public String generateAccessToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());

        return generateToken(extraClaims, user.getUsername(), accessTokenExpiry);
    }

    // Generate refresh token
    @Override
    public String generateRefreshToken(User user) {
        return generateToken(new HashMap<>(), user.getUsername(), refreshTokenExpiry);
    }

}
