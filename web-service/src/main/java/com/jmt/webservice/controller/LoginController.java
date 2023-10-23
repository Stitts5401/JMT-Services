package com.jmt.webservice.controller;

import com.jmt.webservice.model.LoginForm;
import com.jmt.webservice.service.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class LoginController {

    private final WebService webService;

    public LoginController(WebService webService) {
        this.webService = webService;
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
    @PostMapping("/verify-login")
    public Mono<String> loginUser(@ModelAttribute LoginForm loginForm, Model model, ServerWebExchange exchange) {
        //call webservice to authenticate user
        return webService.authenticateAndRedirect(loginForm, model, exchange);
    }


}


