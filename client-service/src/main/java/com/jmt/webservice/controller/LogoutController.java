package com.jmt.webservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@CircuitBreaker(name = "account", fallbackMethod = "fallback")
public class LogoutController {

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;

    // Replace with your Keycloak logout URL and client details
    @Value("${keycloak.logout.url}")
    private String KEYCLOAK_LOGOUT_URL;
    @Value("${keycloak.client.id}")
    private String CLIENT_ID;
    @Value("${keycloak.client.secret}")
    private String CLIENT_SECRET;
    @RequestMapping("/logout")
    public Mono<Void> logout(ServerWebExchange exchange,
                             @AuthenticationPrincipal OAuth2AuthenticationToken oauthToken)
    {
        // Invalidate the session and then redirect
        return exchange.getSession()
                .doOnNext(WebSession::invalidate)
                .then(logoutFromKeycloak(oauthToken))
                .then(Mono.defer(() -> {
                    // After logout, redirect to the home page
                    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                    exchange.getResponse().getHeaders().setLocation(URI.create("/home"));
                    return exchange.getResponse().setComplete();
                }));
    }

    private Mono<Void> logoutFromKeycloak(OAuth2AuthenticationToken oauthToken) {
        return authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName())
                .flatMap(authorizedClient -> {
                    String logoutUri = UriComponentsBuilder.fromUriString(KEYCLOAK_LOGOUT_URL)
                            .buildAndExpand(authorizedClient.getClientRegistration().getProviderDetails().getIssuerUri())
                            .toUriString();

                    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                    formData.add("client_id", CLIENT_ID);
                    formData.add("client_secret", CLIENT_SECRET); // If client is confidential
                    formData.add("refresh_token", authorizedClient.getRefreshToken().getTokenValue());

                    return WebClient.create()
                            .post()
                            .uri(logoutUri)
                            .bodyValue(formData)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .onErrorResume(e -> Mono.empty()); // Ignore errors in the logout process
                });
    }
}