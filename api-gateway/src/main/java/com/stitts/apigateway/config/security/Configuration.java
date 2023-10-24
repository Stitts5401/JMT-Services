package com.stitts.apigateway.config.security;

import com.stitts.apigateway.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@org.springframework.context.annotation.Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class Configuration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**","/eureka**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final ReactiveAuthenticationManager authenticationManager;
    private final LogoutService logoutService;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeExchange(req ->
                        req.pathMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .pathMatchers("/admin/**").hasRole("ADMIN")
                                .pathMatchers("/user/**").hasRole("USER")
                                .pathMatchers("/contractor/**").hasRole("CONTRACTOR")
                                .pathMatchers("/create/**").hasRole("CREATOR")
                                .anyExchange().authenticated()
                )
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .logoutHandler(logoutService)
                );
        return http.build();
    }

}
