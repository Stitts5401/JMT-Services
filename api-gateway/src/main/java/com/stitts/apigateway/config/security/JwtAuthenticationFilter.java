package com.stitts.apigateway.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

  private final JwtService jwtService;
  private final CustomUserDetailsService userDetailsService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("Inside JwtAuthenticationFilter");
        if (exchange.getRequest().getPath().value().contains("/api/v1/auth")) {

            log.info("Inside JwtAuthenticationFilter - /api/v1/auth");
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        log.info("Inside JwtAuthenticationFilter - userEmail: " + userEmail);
        return userDetailsService.findUserWithRolesByEmail(userEmail)
                .flatMap(reactiveUserDetails -> {
                    if (jwtService.validateToken(jwt, reactiveUserDetails.getToken().getTokenValue())) {
                        // Authenticate the user if the JWT is valid
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                reactiveUserDetails, null, reactiveUserDetails.getAuthorities());

                        // Store authentication in the reactive context instead of the SecurityContextHolder
                        SecurityContextImpl securityContext = new SecurityContextImpl(authentication);
                        return chain.filter(exchange)
                                .contextWrite(Context.of(SecurityContext.class, securityContext));
                    } else {
                        // Return unauthorized response if the JWT is invalid
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                })
                .switchIfEmpty(Mono.fromRunnable(() -> {
                    // If no user details found, return unauthorized response
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                }))
                .then(chain.filter(exchange));
    }



}

