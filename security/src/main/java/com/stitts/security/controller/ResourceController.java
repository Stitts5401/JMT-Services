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
        return Mono.just("redirect:/index.html");
    }
    /** Home page. */
    @RequestMapping("/index.html")
    public Mono<String> index() {
        return Mono.just("index");
    }
    /** Sign in page. */
    @RequestMapping("/sign-up.html")
    public Mono<String> signUp() {
        return Mono.just("sign-up");
    }
    /** Simulation of an exception. */
    @RequestMapping("/simulateError.html")
    public void simulateError() {
        throw new RuntimeException("This is a simulated error message");
    }
    /** Error page. */
    @RequestMapping("/error.html")
    public Mono<String> forbidden() {
        return Mono.just("error");
    }
}