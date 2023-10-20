package com.stitts.security.controller.roles;

import com.stitts.security.entity.User;
import com.stitts.security.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/contractor")
public class ContractorController {
    private final UserService userService;
    public ContractorController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping(value = "/account-profile")
    public Mono<String> contractorHome() {
        return Mono.just("contractor/account-profile");
    }
    @GetMapping("/{email}")
    public Mono<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }
}