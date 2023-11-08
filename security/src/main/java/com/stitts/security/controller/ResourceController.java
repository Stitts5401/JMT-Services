package com.stitts.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Controller
public class ResourceController {
    @RequestMapping("/")
    public Mono<String> root(Locale locale) {
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
    /** Error page. */
    @RequestMapping("/error")
    public Mono<String> forbidden() {
        return Mono.just("error");
    }
}