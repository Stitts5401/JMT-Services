package com.jmt.webservice.controller;

import com.jmt.webservice.model.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping("/")
public class LoginController {
    /** Home page. */
    @RequestMapping("/")
    public Mono<String> root() {
        return Mono.just("home");
    }    /** Home page. */
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
            return Mono.just("login");
        });
    }
}


