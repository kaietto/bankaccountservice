package com.bankaccount.service.config;

import com.bankaccount.service.dto.BusinessErrorDto;
import com.bankaccount.service.enumeration.BusinessMessage;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private String secret;

    public AuthorizationFilter(AuthenticationManager authenticationManager, String secret) {
        super(authenticationManager);
        this.secret = secret;
    }

    // @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = getAuthentication(request);
        if (authentication == null) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && !token.isBlank() && token.startsWith("Bearer ")) {
            try {
                byte[] signingKey = secret.getBytes();
                Jws<Claims> parsedToken = null;
                String username = null;
                try {
                    parsedToken = Jwts.parserBuilder()
                                    .setSigningKey(signingKey)
                                    .build()
                                    .parseClaimsJws(token.replace("Bearer ", ""));
                    username = parsedToken.getBody().getSubject();
                } catch (Exception ex) {
                    BusinessErrorDto error = new BusinessErrorDto();
                    error.setCode(BusinessMessage.ACCESSDENIED.name());
                    error.setMessage(ex.getMessage());
                    error.setDetailedMessage(Arrays.toString(ex.getStackTrace()));
                    error.setStatus(HttpStatus.FORBIDDEN.value());
                }
            } catch (ExpiredJwtException exception) {
                log.warn("JWT token expired", exception);
            } catch (UnsupportedJwtException exception) {
                log.warn("Unsupported JWT", exception);
            } catch (MalformedJwtException exception) {
                log.warn("Invalid JWT", exception);
            } catch (SecurityException exception) {
                log.warn("JWT with invalid signature", exception);
            } catch (IllegalArgumentException exception) {
                log.warn("Empty or null JWT", exception);
            }
        }
        return null;
    }

}

