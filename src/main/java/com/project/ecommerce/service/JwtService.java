package com.project.ecommerce.service;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.project.ecommerce.model.User;

import io.jsonwebtoken.Claims;

public interface JwtService {

    /**
     * Extract all claims from the token.
     * 
     * @param token the String containing token.
     * @return the Claims extracted from the token.
     */
    public Claims extractClaims(String token);

    /**
     * extract a specific claim from the token.
     * 
     * @param <T> the type of the claim to be extracted.
     * @param token the String containing token.
     * @param claimsResolver the function to extract the claim.
     * @return the claim extracted from the token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Extract username from the token.
     * 
     * @param token the String containing token.
     * @return the username extracted from the token.
     */
    public String extractUsername(String token);

    /**
     * Check if the token is expired.
     * 
     * @param token the String containing token.
     * @return true if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token);

    /**
     * Validate the token.
     * 
     * @param token the String containing token.
     * @param userDetails the UserDetails object.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Generate token with extra claims, subject and expiration time.
     * 
     * @param extraClaims the extra claims to be added to the token.
     * @param subject the subject of the token.
     * @param expiration in milliseconds
     * @return the generated token.
     */
    public String generateToken(Map<String, Object> extraClaims, String subject, long expiration);

    /**
     * Generate access token
     * 
     * @param user the User object.
     * @return the generated access token.
     */
    public String generateAccessToken(User user);

    /**
     * Generate refresh token
     * 
     * @param user the User object.
     * @return the generated refresh token.
     */
    public String generateRefreshToken(User user);

}
