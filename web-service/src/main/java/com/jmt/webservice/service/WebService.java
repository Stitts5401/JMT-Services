package com.jmt.webservice.service;

import com.jmt.webservice.model.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WebService{
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private JwtTokenUtil jwtUtil;

    @Value("${gateway.url:http://localhost:8080/}")
    private String GATEWAY_URL;
    public Mono<String> authenticateAndRedirect(LoginForm loginForm, Model model, ServerWebExchange exchange) {

        WebClient webClient = webClientBuilder.baseUrl(GATEWAY_URL).build();

        // Send credentials to API Gateway
        return webClient.post()
                .uri("/authenticate")
                .bodyValue(loginForm)
                .header(HttpHeaders.CONTENT_TYPE, "application/json" )
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(jwtToken -> {
                    log.info("JWT Token: " + jwtToken);
                    exchange.getSession().doOnNext(session -> session.getAttributes().put("jwtToken", jwtToken))
                            .subscribe();
                    // Decode JWT to determine user's role and redirect
                    String role = jwtUtil.decodeRoleFromJwt(jwtToken); // This function decodes JWT and extracts role
                    log.info("Role: " + role);
                    return Mono.just("redirect:/" + jwtUtil.determineRedirectPathBasedOnRole(role));
                })
                .onErrorResume(e -> {
                    model.addAttribute("error", "Invalid credentials");
                    return Mono.just("login");  // Return back to the login page with error
                });
    }

}
