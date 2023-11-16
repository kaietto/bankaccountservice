package com.bankaccount.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Claudio Menin
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    private static final String[] SWAGGER_PATHS = {
        "/swagger-ui.html", 
        "/v3/api-docs/**", 
        "/swagger-ui/**", 
        "/webjars/swagger-ui/**"
    };
    private static final String[] API_PATHS = {
            "/api/interface/health"
    };
    
    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(SWAGGER_PATHS).permitAll()
                    .requestMatchers(API_PATHS).permitAll()
                    .anyRequest().authenticated())
            .build();
    }
}