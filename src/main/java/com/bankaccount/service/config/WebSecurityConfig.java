package com.bankaccount.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author Claudio Menin
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    private static final RequestMatcher SWAGGER_PATHS = new AntPathRequestMatcher("/swagger-ui.html");
    private static final RequestMatcher SWAGGER_API_PATHS = new AntPathRequestMatcher("/v3/api-docs/**");
    private static final RequestMatcher SWAGGER_UI_PATHS = new AntPathRequestMatcher("/swagger-ui/**");
    private static final RequestMatcher SWAGGER_WEBJARS_PATHS = new AntPathRequestMatcher("/webjars/swagger-ui/**");
    private static final String API_INTERFACE = "/api/interface";

    private static final RequestMatcher[] SWAGGER_MATCHERS = {
            SWAGGER_PATHS,
            SWAGGER_API_PATHS,
            SWAGGER_UI_PATHS,
            SWAGGER_WEBJARS_PATHS
    };

    private static final RequestMatcher[] API_MATCHERS = {
            new AntPathRequestMatcher(API_INTERFACE + "/health"),
            new AntPathRequestMatcher(API_INTERFACE + "/getDemoToken"),
            new AntPathRequestMatcher(API_INTERFACE + "/getAccountBalance/{accountId}")
    };

    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER_MATCHERS).permitAll()
                        .requestMatchers(API_MATCHERS).permitAll()
                        .anyRequest().authenticated())
                .build();
    }
}
