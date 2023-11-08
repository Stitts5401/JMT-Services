package com.stitts.security.controller;

import com.stitts.security.entity.CustomUserDetails;
import com.stitts.security.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

@Slf4j
@Controller
public class LoginController {
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    public LoginController(ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }

    @GetMapping("/login")
    public Mono<String> login(ServerWebExchange exchange, Model model) {
        if (exchange.getAttribute("notFound") != null) {
            model.addAttribute("notFound", true);
        }
        if (exchange.getAttribute("loginError") != null) {
            model.addAttribute("loginError", true);
        }
        return Mono.just("login"); // assuming "login" is the name of your login view
    }

    @GetMapping("/login-error")
    public Mono<String> loginError(Model model) {
        model.addAttribute("loginError", Boolean.TRUE);
        return Mono.just("/login-error");
    }

    @PostMapping("/login")
    public Mono<Void> loginUser(@RequestParam String username,
                                @RequestParam String password,
                                ServerWebExchange exchange,
                                WebSession webSession) {  // Add the WebSession parameter here
        return Mono.just(new UsernamePasswordAuthenticationToken(username, password))
                .flatMap(reactiveAuthenticationManager::authenticate)
                .flatMap(authenticated -> {
                    // Use the injected WebSession here
                    webSession.getAttributes().put("username", username);
                    return Mono.empty();
                })
                .onErrorResume(UserNotFoundException.class, ex -> {
                    return handleLoginError(exchange, "notFound");
                })
                .onErrorResume(AuthenticationException.class, ex -> {
                    return handleLoginError(exchange, "loginError");
                }).then();
    }

    private Mono<Void> handleLoginError(ServerWebExchange exchange, String errorAttribute) {
        return exchange.getSession()
                .doOnNext(session -> {
                    session.getAttributes().put(errorAttribute, true);
                })
                .then(redirectToLoginError(exchange));
    }


    private Mono<Void> redirectToLoginError(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
        exchange.getResponse().getHeaders().setLocation(URI.create("/login"));
        return exchange.getResponse().setComplete();
    }

}

