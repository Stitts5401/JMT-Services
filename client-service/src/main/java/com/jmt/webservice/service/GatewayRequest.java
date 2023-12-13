package com.jmt.webservice.service;

import net.minidev.json.JSONObject;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;



@Component
public class GatewayRequest {

    private final WebClient webClient;
    private final ReactiveOAuth2AuthorizedClientService authorizedClientRepository;

    public GatewayRequest(WebClient.Builder webClientBuilder,
                           ReactiveOAuth2AuthorizedClientService authorizedClientRepository) {
        this.webClient = webClientBuilder.baseUrl("http://api-gateway:8282").build();
        this.authorizedClientRepository = authorizedClientRepository;
    }

    public Mono<Void> post(OAuth2AuthenticationToken token, String url, JSONObject body) {
        String clientRegistrationId = token.getAuthorizedClientRegistrationId();
        String principalName = token.getName();

        return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName).flatMap
                (client -> {
                    OAuth2AccessToken accessToken = client.getAccessToken();
                    String jwtToken = accessToken.getTokenValue(); // The JWT token
                    return webClient.post()
                            .uri(url)
                            .headers(headers -> headers.setBearerAuth(jwtToken))
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(Void.class);
                });
    }    public Mono<Void> post(OAuth2AuthenticationToken token, String url, String body) {
        String clientRegistrationId = token.getAuthorizedClientRegistrationId();
        String principalName = token.getName();

        return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName).flatMap
                (client -> {
                    OAuth2AccessToken accessToken = client.getAccessToken();
                    String jwtToken = accessToken.getTokenValue(); // The JWT token
                    return webClient.post()
                            .uri(url)
                            .headers(headers -> headers.setBearerAuth(jwtToken))
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(Void.class);
                });
    }
}
