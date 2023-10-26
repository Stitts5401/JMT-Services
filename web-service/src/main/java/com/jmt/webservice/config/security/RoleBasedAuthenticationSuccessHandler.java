package com.jmt.webservice.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collection;

public class RoleBasedAuthenticationSuccessHandler extends RedirectServerAuthenticationSuccessHandler {
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        // Get the authenticated user's authorities
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl;  // Default path

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            redirectUrl = "/admin";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            redirectUrl = "/user";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CONTRACTOR"))) {
            redirectUrl = "/contractor";
        } else {
            redirectUrl = "/default";
        }
        return webFilterExchange.getExchange().getSession()
                .then(Mono.fromRunnable(() -> {
                    setLocation(URI.create(redirectUrl));
                })).then(super.onAuthenticationSuccess(webFilterExchange, authentication));
    }
}
