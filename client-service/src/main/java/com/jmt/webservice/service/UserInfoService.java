package com.jmt.webservice.service;

import com.jmt.webservice.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
@Service
@Slf4j
public class UserInfoService {

    private final WebClient webClient;
    private final ReactiveOAuth2AuthorizedClientService authorizedClientRepository;

        public UserInfoService(WebClient.Builder webClientBuilder,
                               ReactiveOAuth2AuthorizedClientService authorizedClientRepository) {
            this.webClient = webClientBuilder.baseUrl("http://localhost:8282").build();
            this.authorizedClientRepository = authorizedClientRepository;
        }
        public Mono<UserInfo> retrieveUserInfo(OAuth2AuthenticationToken oauthToken) {
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            String principalName = oauthToken.getName();

            return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName)
                    .flatMap(this::fetchUserInfoFromResourceServer);
        }

        private Mono<UserInfo> fetchUserInfoFromResourceServer(OAuth2AuthorizedClient authorizedClient) {
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            String jwtToken = accessToken.getTokenValue(); // The JWT token

            // Handle specific errors if necessary
            return webClient.get()
                    .uri("/user/info")
                    .headers(headers -> headers.setBearerAuth(jwtToken))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Expired or invalid JWT token"))
                    )
                    .bodyToMono(UserInfo.class)
                    .onErrorResume(Mono::error);
        }

    }
