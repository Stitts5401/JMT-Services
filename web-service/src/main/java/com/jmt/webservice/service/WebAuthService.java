package com.jmt.webservice.service;

import com.jmt.webservice.model.AuthenticationRequest;
import com.jmt.webservice.model.AuthenticationResponse;
import com.jmt.webservice.model.ErrorResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.util.Map;

@Service
public class WebAuthService {

    private WebClient webClient;

    @Value("${api.gateway.url}")
    private String API_GATEWAY_AUTH_URL;

    private final WebClient.Builder webClientBuilder;

    public WebAuthService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.baseUrl(API_GATEWAY_AUTH_URL).build();
    }

    public Mono<ResponseEntity<AuthenticationResponse>> authenticate(AuthenticationRequest request) {
        return webClient.post()
                .uri("/authenticate")
                .bodyValue(request)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.toEntity(AuthenticationResponse.class);
                    } else {
                        return response.toEntity(ErrorResponse.class)
                                .flatMap(errorResponse
                                        -> Mono.error(new AuthenticationException(
                                                errorResponse.getBody().getMessage())));
                    }
                });
        //TODO: Refactor response deserialize logic
    }
}
