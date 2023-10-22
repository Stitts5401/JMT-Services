package com.stitts.apigateway.config;

import com.stitts.apigateway.service.CustomReactiveUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private CustomReactiveAuthenticationManager customReactiveAuthenticationManager;

    // constructor
    public SecurityConfig(CustomReactiveAuthenticationManager customReactiveAuthenticationManager) {
        this.customReactiveAuthenticationManager = customReactiveAuthenticationManager;
    }
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/public/**", "/login", "/logout").permitAll()
                .and().authenticationManager(customReactiveAuthenticationManager)
                .authorizeExchange()
                .pathMatchers("/user/**").hasRole("USER")
                .pathMatchers("/contractor/**").hasRole("CONTRACTOR")
                .pathMatchers("/create/**").hasRole("CREATOR")
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and()
                .formLogin();

        return http.build();
    }
}
