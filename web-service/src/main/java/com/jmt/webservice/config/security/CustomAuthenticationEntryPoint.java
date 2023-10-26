package com.jmt.webservice.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;


@Slf4j
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.error("Authentication failed", ex);
        if (ex instanceof BadCredentialsException) {
            return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
        } else if (ex instanceof AccountExpiredException) {
            return Mono.fromRunnable(() -> {
               exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
               exchange.getResponse().getHeaders().setLocation(URI.create("/session-expired"));
            });
        }
        // handle other types of exceptions or provide a default behavior
        return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
    }
}