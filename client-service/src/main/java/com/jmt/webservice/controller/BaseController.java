package com.jmt.webservice.controller;

import com.jmt.webservice.literal.NationalityData;
import com.jmt.webservice.service.GoogleCloudStorageService;
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
public class BaseController {

    private final UserInfoService userInfoService;
    public String fallbackMethod(Exception e) {
        log.error("Fallback method triggered with exception {}", e.getMessage());
        return "fallback";
    }

    /**
     * Home page redirect.
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
     * Welcome page.
     * Redirect to auth account info or view jobs.
     */
    @RequestMapping("/welcome-directory")
    public Mono<String> welcome(Model model, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {
        return oauthTokenMono
                .flatMap(userInfoService::retrieveUserInfo).map( userInfo -> {
                    model.addAttribute("userInfo", userInfo);
                    return "welcome-directory";
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No AuthenticationPrincipal found, redirecting to Keycloak");
                    return Mono.just("redirect:/oauth2/authorization/keycloak");
                }))
                .doOnNext(viewName -> log.info("Rendering view: " + viewName))
                .onErrorResume(e -> {
                    log.error("Error while processing the welcome request: ", e);
                    model.addAttribute("loginError", true);
                    return Mono.just("home");
                });
    }
    @RequestMapping("/privacy-policy")
    public Mono<String> privacyPolicy(Model model){
        return Mono.just("privacy-policy");
    }
    @RequestMapping("/terms-of-service")
    public Mono<String> termsOfService(Model model){
        return Mono.just("terms-of-service");
    }
    @RequestMapping("/contact")
    public Mono<String> getContactPage(Model model){
        return Mono.just("contact");
    }
    @RequestMapping("/pricing")
    public Mono<String> deleteAccount(Model model){
        return Mono.just("pricing");
    }
}





