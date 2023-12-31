package com.jmt.webservice.service;

import com.jmt.model.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoService {


    @Value("${host.gateway}")
    private String hostGateway;

    private final WebClient.Builder webClient;
    private final ReactiveOAuth2AuthorizedClientService authorizedClientRepository;
    private final GoogleCloudStorageService googleCloudStorageService;

        public Mono<UserInfo> retrieveUserInfo(OAuth2AuthenticationToken oauthToken) {
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            String principalName = oauthToken.getName();

            return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName)
                    .flatMap(this::fetchUserInfoFromResourceServer)
                    .doOnNext(userInfo -> log.info("Retrieved user info: " + userInfo))
                    .flatMap(userInfo -> {
                        Mono<String> blobNameMono = Mono.just(userInfo.getBlobName() == null || userInfo.getBlobName().isEmpty() ?
                                "01.jpg" :
                                userInfo.getBlobName());
                        return blobNameMono.flatMap(data -> googleCloudStorageService.generateSignedUrl(data , false))
                                .map(signedUrl -> {
                                    userInfo.setBlobName(signedUrl);
                                    return userInfo;
                                })
                                .onErrorReturn( userInfo );
                    } );
        }

        private Mono<UserInfo> fetchUserInfoFromResourceServer(OAuth2AuthorizedClient authorizedClient) {
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            String jwtToken = accessToken.getTokenValue(); // The JWT token

            // Handle specific errors if necessary
            return webClient.build().get()
                    .uri(hostGateway + "/user/info")
                    .headers(headers -> {
                            headers.setBearerAuth(jwtToken);
                            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                    })
                    .exchangeToMono(response -> {
                        log.info("Response status: " + response.toString());
                            if (response.statusCode().isError()) {
                                    return response.createException()
                                            .flatMap(Mono::error);
                            } else {
                                    return response.bodyToMono(UserInfo.class);
                            }
                    }) ;
        }
    }
