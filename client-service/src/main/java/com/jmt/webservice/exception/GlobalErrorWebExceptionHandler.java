package com.jmt.webservice.exception;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
public class GlobalErrorWebExceptionHandler implements WebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof ValidationException) {
            // Handle validation exception, set response status, etc.
        }
        // Handle other exceptions...
        return Mono.error(ex);
    }
}
