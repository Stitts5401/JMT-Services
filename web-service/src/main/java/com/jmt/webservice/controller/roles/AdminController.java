package com.jmt.webservice.controller.roles;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping( "/account-profile")
    public Mono<String> adminHome() {
        return Mono.just("account-profile");
    }
}