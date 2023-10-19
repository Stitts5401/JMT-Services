package com.stitts.security.controller;

import com.stitts.security.entity.User;
import com.stitts.security.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    /** User zone index. */
    @GetMapping("/account-profile.html")
    public Mono<String> userIndex() {
        return Mono.just("account-profile");
    }
    /** Administration zone index. */
    @GetMapping("/admin/account-profile.html")
    public Mono<String> adminIndex() {
        return Mono.just("/admin/account-profile");
    }
    @GetMapping("/{email}")
    public Mono<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    // ... other endpoints related to User
}
