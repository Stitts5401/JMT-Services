package com.stitts.security.controller;

import com.stitts.security.entity.CustomUserDetails;
import com.stitts.security.exceptions.UserNotFoundException;
import com.stitts.security.forms.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.Collection;

@Slf4j
@Controller
public class LoginController {
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    public LoginController(ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }

    @GetMapping("/login")
    public Mono<String> login(ServerWebExchange exchange, Model model) {
        return exchange.getSession().flatMap(session -> {
            if (session.getAttribute("notFound") != null) {
                model.addAttribute("notFound", true);
                session.getAttributes().remove("notFound");
            }
            if (session.getAttribute("loginError") != null) {
                model.addAttribute("loginError", true);
                session.getAttributes().remove("loginError");
            }
            return Mono.just("login"); // assuming "login" is the name of your login view
        });
    }

    @GetMapping("/login-error")
    public Mono<String> loginError(Model model) {
        model.addAttribute("loginError", Boolean.TRUE);
        return Mono.just("/login-error");
    }

    @PostMapping("/loginProcess")
    public Mono<Void> loginUser(@ModelAttribute LoginForm loginForm, ServerWebExchange exchange) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        return Mono.just(new UsernamePasswordAuthenticationToken(username, password))
                .flatMap(reactiveAuthenticationManager::authenticate)
                .doOnNext(auth -> {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                })
                .flatMap(auth -> redirectToBasedOnRole(auth, exchange))
                .onErrorResume(UserNotFoundException.class, ex -> {
                    log.info("User {} attempted to log in.", username);
                    log.error("User not found {}: {}", username, ex.getMessage());
                    return handleLoginError(exchange, "notFound");
                })
                .onErrorResume(AuthenticationException.class, ex -> {
                    log.info("User {} attempted to log in.", username);
                    log.error("Login error for user {}: {}", username, ex.getMessage());
                    return handleLoginError(exchange, "loginError");
                });
    }

    private Mono<Void> redirectToBasedOnRole(Authentication auth, ServerWebExchange exchange) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String redirectUrl = getRedirectUrlBasedOnAuthorities(authorities);
        log.info("Redirecting user to: {}", redirectUrl);

        exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
        exchange.getResponse().getHeaders().setLocation(URI.create(redirectUrl));
        return exchange.getResponse().setComplete();
    }

    private String getRedirectUrlBasedOnAuthorities(Collection<? extends GrantedAuthority> authorities) {
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "/admin/account-profile";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return "/user/account-profile";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CONTRACTOR"))) {
            return "/contractor/account-profile";
        } else {
            log.error("User is not an admin, user, or contractor. Redirecting to 403.");
            return "/403";
        }
    }
    private Mono<Void> handleLoginError(ServerWebExchange exchange, String errorAttribute) {
        return exchange.getSession()
                .flatMap(session -> {
                    session.getAttributes().put(errorAttribute, true);
                    return session.save();
                })
                .then(redirectToLoginError(exchange));
    }
    private Mono<Void> redirectToLoginError(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
        exchange.getResponse().getHeaders().setLocation(URI.create("/login"));
        return exchange.getResponse().setComplete();
    }

}


