package com.stitts.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collection;

public class RoleBasedAuthenticationSuccessHandler extends RedirectServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();

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
        return exchange.getSession()
                .then(Mono.fromRunnable(() -> {
                    // Set the default location using the base class's method
                    setLocation(URI.create(redirectUrl));
                }))
                // Call the base class's onAuthenticationSuccess method
                .then(super.onAuthenticationSuccess(webFilterExchange, authentication));
    }
}
