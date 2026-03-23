package com.project.ecommerce.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.ecommerce.service.CustomUserDetailsService;
import com.project.ecommerce.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException, ServletException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Skip JWT check for public endpoints
        if (path.startsWith("/api/products") && method.equals("GET")
            || path.startsWith("/api/categories") && method.equals("GET")
            || path.startsWith("/auth")
            || path.startsWith("/uploads")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String header = request.getHeader("Authorization");
        final String token;
        final String username;

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = header.substring(7);

        try {
            username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    sendError(response, "Invalid token!", HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        } catch (ExpiredJwtException e) {
            sendError(response, "Token expired, login again!", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception e) {
            sendError(response, "Invalid token!", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        response.getWriter().write(
                "{ \"statusCode\": " + status +
                        ", \"error\": \"Unauthorized\", " +
                        "\"message\": \"" + message + "\" }"
        );
    }
}
