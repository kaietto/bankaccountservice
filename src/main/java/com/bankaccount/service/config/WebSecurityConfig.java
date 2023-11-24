package com.bankaccount.service.config;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
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
            new AntPathRequestMatcher(API_INTERFACE + "/getAccountBalance/{accountId}"),
            new AntPathRequestMatcher(API_INTERFACE + "/createMoneyTransfer/{accountId}"),
            new AntPathRequestMatcher(API_INTERFACE + "/getCashAccountTransactions/{accountId}")
    };

    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // [ClaudioM] Spring Security 6.0 -> needed to disable CSRF protection and avoid POST 403 Forbidden error
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER_MATCHERS).permitAll()
                        .requestMatchers(API_MATCHERS).permitAll()
                        .anyRequest().authenticated()
                ).build();
    }
}
