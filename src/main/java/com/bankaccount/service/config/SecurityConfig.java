package com.bankaccount.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Claudio Menin
 */

@Configuration
public class SecurityConfig {
    @Bean
    public JwtDecoder jwtDecoder() {
        // Claudio: Provide a dummy JwtDecoder (not suitable for Production, only for DEMO semplification purposes)
        String tokenValue = "dummyTokenValue";
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(3600); // Assuming the token is valid for 1 hour
        // Dummy headers and claims
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");
        // Dummy sub and user
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "1234567890");
        claims.put("name", "Paolo Rossi");
        return token -> new Jwt(tokenValue, issuedAt, expiresAt, headers, claims);
    }
}