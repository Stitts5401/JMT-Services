package com.stitts.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Slf4j
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        // Logging the exception for debugging purposes
        log.error("Authentication error: ", e);

        // Differentiate between exceptions
        if (e instanceof InsufficientAuthenticationException) {
            // This indicates a user is not authenticated
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        } else {
            // Handle other types of AuthenticationException or default behavior
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        }

        // You can further enhance by returning a custom error response, if desired

        return exchange.getResponse().setComplete();
    }
}
