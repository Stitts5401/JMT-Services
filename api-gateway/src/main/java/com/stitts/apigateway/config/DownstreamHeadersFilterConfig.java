package com.stitts.apigateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
public class DownstreamHeadersFilterConfig {

    @Bean
    @Order(-1)
    public GlobalFilter downstreamHeadersFilter() {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    String preferredUsername = null;
                    Collection<GrantedAuthority> rolesList = null;

                    if (authentication instanceof JwtAuthenticationToken) {
                        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                        preferredUsername = jwt.getClaimAsString("email");
                        rolesList = ((JwtAuthenticationToken) authentication).getAuthorities();
                    }

                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-Preferred-Username", preferredUsername)
                            .header("X-Authorities", rolesList.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.joining(",")))
                            .build();
                    return exchange.mutate().request(request).build();
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }



}
