package com.jmt.webservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {
    @GetMapping(value = "/account-profile")
    public Mono<String> userHome() {
        return Mono.just("/user/account-profile");
    }

}
