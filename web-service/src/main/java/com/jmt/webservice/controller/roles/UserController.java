package com.jmt.webservice.controller.roles;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping(value = "/account-profile")
    public Mono<String> userHome() {
        return Mono.just("/user/account-profile");
    }

}
