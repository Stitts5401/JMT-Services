package com.jmt.webservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping("/")
public class LoginController {
    /** Home page. */
    @RequestMapping("")
    public Mono<String> root() {
        return Mono.just("redirect:/home");
    }
    /** Home page. */
    @RequestMapping("/home")
    public Mono<String> home() {
        return Mono.just("home");
    }
    /** Sign in page. */
    @RequestMapping("/sign-up")
    public Mono<String> signUp() {
        return Mono.just("sign-up");
    }
    /** Simulation of an exception. */
    @RequestMapping("/simulateError")
    public void simulateError() {
        throw new RuntimeException("This is a simulated error message");
    }
    @GetMapping("/welcome-directory")
    public Mono<String> welcome (@AuthenticationPrincipal OAuth2AuthenticationToken oauthToken) {

        if (oauthToken == null) {
            return Mono.just("redirect:/oauth2/authorization/keycloak");
        }
        return Mono.just("welcome-directory");
    }


}


