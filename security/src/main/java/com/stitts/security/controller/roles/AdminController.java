package com.stitts.security.controller.roles;


import com.stitts.security.entity.User;
import com.stitts.security.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    public AdminController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping( "/account-profile")
    public Mono<String> adminHome() {
        return Mono.just("account-profile");
    }
    @GetMapping("/{email}")
    public Mono<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }
}
