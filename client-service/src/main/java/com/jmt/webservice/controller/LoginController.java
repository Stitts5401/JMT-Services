package com.jmt.webservice.controller;

import com.jmt.webservice.service.UserInfoService;
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
@RequiredArgsConstructor
public class LoginController {

    private final UserInfoService userInfoService;

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
    public Mono<String> welcome(Model model, @AuthenticationPrincipal OAuth2AuthenticationToken oauthToken) {
        if (oauthToken == null) {
            return Mono.just("redirect:/oauth2/authorization/keycloak");
        }
        return userInfoService.retrieveUserInfo(oauthToken)
                .flatMap( userInfo -> {
                    model.addAttribute("name", userInfo.getFirstname() + " " + userInfo.getLastname());
                    model.addAttribute("email", userInfo.getEmail() );
                    return Mono.just("welcome-directory");
                });
        }
    }





