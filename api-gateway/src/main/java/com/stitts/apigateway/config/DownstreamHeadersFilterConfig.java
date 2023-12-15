package com.stitts.apigateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
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
                    String roles = "";

                    if (authentication instanceof JwtAuthenticationToken) {
                        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                        preferredUsername = jwt.getClaimAsString("preferred_username");
                        List<String> rolesList = jwt.getClaimAsStringList("roles");
                        if (rolesList != null) {
                            roles = String.join(",", rolesList);
                        }
                    } else if (authentication instanceof OAuth2AuthenticationToken) {
                        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                        OAuth2AuthenticatedPrincipal principal = oauthToken.getPrincipal();
                        preferredUsername = principal.getAttribute("preferred_username");
                        roles = oauthToken.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(","));
                    }

                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-Preferred-Username", preferredUsername)
                            .header("X-Authorities", roles)
                            .build();
                    return exchange.mutate().request(request).build();
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }



}
