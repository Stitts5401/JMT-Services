package com.stitts.security.config;

import com.stitts.security.handler.RoleBasedAuthenticationSuccessHandler;
import com.stitts.security.service.CustomReactiveUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final CustomReactiveUserDetailsService customReactiveUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    public SecurityConfig(CustomReactiveUserDetailsService customReactiveUserDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.customReactiveUserDetailsService = customReactiveUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        final RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
        final RedirectServerAuthenticationFailureHandler authenticationFailureHandler = new RedirectServerAuthenticationFailureHandler("/login");

        logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/home"));

        return http
                .formLogin().loginPage("/login").authenticationFailureHandler(authenticationFailureHandler)
                .authenticationManager(reactiveAuthenticationManager())
                .and()
                .logout().logoutSuccessHandler(logoutSuccessHandler)
                .and()
                // Authorization Configuration
                .authorizeExchange()
                // Explicitly permit access to static resources and certain routes
                .pathMatchers("/static/**", "/login-error", "/login", "/loginProcess", "/home", "/error", "/about").permitAll()
                // Specific role-based matchers
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/user/**").hasRole("USER")
                .pathMatchers("/contractor/**").hasRole("CONTRACTOR")
                .pathMatchers("/create/**").hasRole("CREATOR")
                .anyExchange().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .build();
    }
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(customReactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

}
