package com.jmt.webservice.service;

import com.jmt.webservice.model.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
public class WebService{
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private JwtTokenUtil jwtUtil;

    public Mono<Void> authenticateAndRedirect(LoginForm loginForm, Model model, ServerWebExchange exchange) {

        WebClient webClient = webClientBuilder.baseUrl("http://api-gateway-url").build();

        // Send credentials to API Gateway
        return webClient.post()
                .uri("/authenticate")
                .bodyValue(loginForm)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(jwtToken -> {
                    exchange.getSession().doOnNext(session -> session.getAttributes().put("jwtToken", jwtToken)).subscribe();
                    // Decode JWT to determine user's role and redirect
                    String role = jwtUtil.decodeRoleFromJwt(jwtToken); // This function decodes JWT and extracts role
                    return Mono.just("redirect:/" + jwtUtil.determineRedirectPathBasedOnRole(role));
                })
                .onErrorResume(e -> {
                    model.addAttribute("error", "Invalid credentials");
                    return Mono.just("login");  // Return back to the login page with error
                }).then();
    }

}
