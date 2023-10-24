package com.stitts.apigateway.config.security;

import com.stitts.apigateway.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

  private final JwtService jwtService;
  private final ReactiveUserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    if (exchange.getRequest().getPath().value().contains("/api/v1/auth")) {
      return chain.filter(exchange);
    }

    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return chain.filter(exchange);
    }

    String jwt = authHeader.substring(7);
    String userEmail = jwtService.extractUsername(jwt);

    return userDetailsService.findByUsername(userEmail)
            .flatMap(userDetails -> {
              return tokenRepository.findByToken(jwt)
                      .filter(t -> !t.isExpired() && !t.isRevoked())
                      .filter(token -> jwtService.isTokenValid(jwt, userDetails))
                      .flatMap(token -> {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        SecurityContext securityContext = new SecurityContextImpl(authToken);
                        return new WebSessionServerSecurityContextRepository().save(exchange, securityContext);
                      });
            })
            .then(chain.filter(exchange))
            .switchIfEmpty(chain.filter(exchange)); // Continue filter chain if no user details found
  }
}

