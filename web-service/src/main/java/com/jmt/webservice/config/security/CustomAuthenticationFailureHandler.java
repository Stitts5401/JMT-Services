package com.jmt.webservice.config.security;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.URI;

public class CustomAuthenticationFailureHandler extends RedirectServerAuthenticationFailureHandler {
    public CustomAuthenticationFailureHandler(String location) {
        super(location);
    }
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {

        return webFilterExchange.getExchange()
                .getSession()
                .flatMap(session -> {
                    webFilterExchange.getExchange().getResponse().getHeaders().setLocation(URI.create("/login"));
                    session.getAttributes().put("authError", true);
                    return webFilterExchange.getExchange().getResponse().setComplete();
                });

    }
}
