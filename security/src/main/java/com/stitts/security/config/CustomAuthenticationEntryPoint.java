package com.stitts.security.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        // Custom logic. For example, redirect to a custom login page or return a specific error response
        return Mono.fromRunnable(() -> {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            // You can also set custom headers or anything else you'd like here
        });
    }
}
