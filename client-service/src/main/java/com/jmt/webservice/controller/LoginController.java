package com.jmt.webservice.controller;

import com.jmt.webservice.service.UserInfoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping("/")
@CircuitBreaker(name = "account", fallbackMethod = "fallbackMethod")
@RequiredArgsConstructor
public class LoginController {

    private final UserInfoService userInfoService;

    public String fallbackMethod(Exception e) {
        log.error("Fallback method triggered with exception {}", e.getMessage());
        return "fallback";
    }

    /**
     * Home page.
     */
    @RequestMapping("")
    public Mono<String> root() {
        return Mono.just("redirect:/home");
    }

    /**
     * Home page.
     */
    @RequestMapping("/home")
    public Mono<String> home() {
        return Mono.just("home");
    }

    /**
     * Sign in page.
     */
    @RequestMapping("/sign-up")
    public Mono<String> signUp() {
        return Mono.just("sign-up");
    }

    /**
     * Simulation of an exception.
     */
    @RequestMapping("/simulateError")
    public void simulateError() {
        throw new RuntimeException("This is a simulated error message");
    }

    @GetMapping("/welcome-directory")
    public Mono<String> welcome(Model model, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {
        return oauthTokenMono
                .flatMap(oauthToken -> userInfoService.retrieveUserInfo(oauthToken)
                        .map(userInfo -> {
                            model.addAttribute("name", userInfo.getFirstname() + " " + userInfo.getLastname());
                            model.addAttribute("email", userInfo.getEmail());
                            return "welcome-directory";
                        }))
                .switchIfEmpty(Mono.defer(() -> Mono.just("redirect:/oauth2/authorization/keycloak")));
    }
    }





