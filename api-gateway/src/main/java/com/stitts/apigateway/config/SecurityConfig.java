package com.stitts.apigateway.config;

import com.stitts.apigateway.service.Imp.AuthenticationService;
import com.stitts.apigateway.service.Imp.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;

import java.net.URI;


@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        final RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
        logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/home"));

        return http
                .formLogin().loginPage("/login")
                .authenticationManager(reactiveAuthenticationManager())
                .and()
                .logout().logoutSuccessHandler(logoutSuccessHandler)
                .and()
                // Authorization Configuration
                .authorizeExchange()
                // Explicitly permit access to static resources and certain routes
                .pathMatchers("/static/**", "/login-error", "/login", "/home", "/error", "/about", "/authenticate", "/verify-login").permitAll()
                // Specific role-based matchers
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/user/**").hasRole("USER")
                .pathMatchers("/contractor/**").hasRole("CONTRACTOR")
                .pathMatchers("/create/**").hasRole("CREATOR")
                .anyExchange().permitAll()
                .and()
                .exceptionHandling()
                .and()
                .build();
    }
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

}
