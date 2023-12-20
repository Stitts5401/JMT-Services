package com.jmt.webservice.service;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;



@Component
@RequiredArgsConstructor
public class GatewayRequest {

    @Value("${host.gateway}")
    private String hostGateway ;
    private final WebClient.Builder webClient;
    private final ReactiveOAuth2AuthorizedClientService authorizedClientRepository;


    public Mono<Void> post(OAuth2AuthenticationToken token, String endpoint, JSONObject body) {
        String clientRegistrationId = token.getAuthorizedClientRegistrationId();
        String principalName = token.getName();

        return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName).flatMap
                (client -> {
                    OAuth2AccessToken accessToken = client.getAccessToken();
                    String jwtToken = accessToken.getTokenValue(); // The JWT token
                    return webClient.build().post()
                            .uri(hostGateway + endpoint)
                            .headers(headers -> headers.setBearerAuth(jwtToken))
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(Void.class);
                });
    }
    public Mono<Void> post(OAuth2AuthenticationToken token, String endpoint, String body) {
        String clientRegistrationId = token.getAuthorizedClientRegistrationId();
        String principalName = token.getName();

        return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName).flatMap
                (client -> {
                    OAuth2AccessToken accessToken = client.getAccessToken();
                    String jwtToken = accessToken.getTokenValue(); // The JWT token
                    return webClient.build().post()
                            .uri(hostGateway + endpoint)
                            .headers(headers -> headers.setBearerAuth(jwtToken))
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(Void.class);
                });
    }
}
