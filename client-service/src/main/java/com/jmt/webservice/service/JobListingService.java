package com.jmt.webservice.service;
import com.jmt.webservice.model.JobInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
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
@RequiredArgsConstructor
public class JobListingService {


    @Value("${host.gateway}")
    private String hostGateway;

    private final WebClient.Builder webClient;
    private final ReactiveOAuth2AuthorizedClientService authorizedClientRepository;


    public Mono<Page<JobInfo>> retrieve(int page, int size, String filter, OAuth2AuthenticationToken oauthToken) {
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        String principalName = oauthToken.getName();

        return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName)
                .flatMap(client -> fetchUserInfoFromResourceServer(client, page, size, filter));
    }
    public Mono<JobInfo> retrieve(long jobId, OAuth2AuthenticationToken oauthToken) {
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        String principalName = oauthToken.getName();

        return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName)
                .flatMap(client -> fetchUserInfoFromResourceServer(client, jobId));
    }

    private Mono<Page<JobInfo>> fetchUserInfoFromResourceServer(OAuth2AuthorizedClient authorizedClient,
            int page, int size, String filter) {

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        String jwtToken = accessToken.getTokenValue(); // The JWT token

        return webClient.build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(hostGateway + "/jobs/listings")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("filter", filter)
                        .build())
                .headers(headers -> headers.setBearerAuth(jwtToken))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Expired or invalid JWT token"))
                )
                .bodyToMono(new ParameterizedTypeReference<Page<JobInfo>>() {})
                .onErrorResume(ex -> {
                    log.error("Error retrieving job listings: ", ex);
                    return Mono.error(ex);
                });
    }

    private Mono<JobInfo> fetchUserInfoFromResourceServer( OAuth2AuthorizedClient authorizedClient, long jobId) {

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        String jwtToken = accessToken.getTokenValue(); // The JWT token

        return webClient.build().get()
                .uri(hostGateway + "/jobs/details/{jobId}", jobId) // Pass jobId as a variable for replacement
                .headers(headers -> headers.setBearerAuth(jwtToken))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Expired or invalid JWT token"))
                )
                .bodyToMono(JobInfo.class)
                .onErrorResume(ex -> {
                    log.error("Error retrieving job listings: ", ex);
                    return Mono.error(ex);
                });
    }


}