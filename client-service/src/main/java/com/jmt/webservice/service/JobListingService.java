package com.jmt.webservice.service;
import com.jmt.webservice.model.JobImageDto;
import com.jmt.webservice.model.JobInfo;
import com.jmt.webservice.model.JobInfoPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobListingService {


    @Value("${host.gateway}")
    private String hostGateway;

    private final WebClient.Builder webClient;
    private final ReactiveOAuth2AuthorizedClientService authorizedClientRepository;
    private final GoogleCloudStorageService googleCloudStorageService;


    public Mono<JobInfoPage> retrieve(int page, int size, String filter, OAuth2AuthenticationToken oauthToken) {
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        String principalName = oauthToken.getName();
        Pageable pageable = PageRequest.of(page, size); // Create pageable instance

        return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName)
                .flatMap(client -> fetchUserInfoFromResourceServer(client, pageable, filter))
                        .flatMap(jobInfoPage -> {
                            Flux<JobInfo> jobInfoFlux = Flux.fromIterable(jobInfoPage.getContent());

                            return jobInfoFlux
                                    .flatMap(jobInfo -> {
                                        Flux<JobImageDto> jobImagesFlux = Flux.fromIterable(jobInfo.getImage());
                                        return jobImagesFlux
                                                .flatMap(jobImageDto -> googleCloudStorageService.generateSignedUrl(jobImageDto.getGuid(), true)
                                                        .map(signedUrl -> {
                                                            jobImageDto.setSigned_Url(signedUrl);
                                                            return jobImageDto;
                                                        }))
                                                .collectList()
                                                .map(signedJobImages -> {
                                                    jobInfo.setImage(signedJobImages);
                                                    return jobInfo;
                                                });
                                    })
                                    .collectList()
                                    .map(signedJobInfos -> {
                                        jobInfoPage.setContent(signedJobInfos);
                                        return jobInfoPage;
                                    });
                        });
    }
    public Mono<JobInfo> retrieve(long jobId, OAuth2AuthenticationToken oauthToken) {
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        String principalName = oauthToken.getName();

        return authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principalName)
                .flatMap(client -> fetchUserInfoFromResourceServer(client, jobId))
                .flatMap(jobInfo -> {
                    Flux<JobImageDto> jobImagesFlux = Flux.fromIterable(jobInfo.getImage());

                    return jobImagesFlux
                            .flatMap(jobImageDto -> googleCloudStorageService.generateSignedUrl(jobImageDto.getGuid(), true)
                                    .map(signedUrl -> {
                                        jobImageDto.setSigned_Url(signedUrl);
                                        return jobImageDto;
                                    }))
                            .collectList()
                            .map(signedJobImages -> {
                                jobInfo.setImage(signedJobImages);
                                return jobInfo;
                            });
                });
    }
    private Mono<JobInfoPage> fetchUserInfoFromResourceServer(OAuth2AuthorizedClient authorizedClient,
                                                              Pageable pageable, String filter) {

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        String jwtToken = accessToken.getTokenValue(); // The JWT token

        return webClient.build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(hostGateway + "/jobs/listings")
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .queryParam("filter", filter)
                        .build())
                .headers(headers -> headers.setBearerAuth(jwtToken))
                .retrieve()
                .bodyToMono(JobInfoPage.class) // Adjust this line
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
                        Mono.error(
                                new ResponseStatusException(clientResponse.statusCode()
                                , "Expired or invalid JWT token"))
                )
                .bodyToMono(JobInfo.class)
                .doOnNext(jobInfo -> log.info("JobInfo: {}", jobInfo))
                .log()
                .onErrorResume(ex -> {
                    log.error("Error retrieving job listings: ", ex);
                    return Mono.error(ex);
                });
    }


}