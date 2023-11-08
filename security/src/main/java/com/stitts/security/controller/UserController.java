package com.stitts.security.controller;

import com.stitts.security.entity.User;
import com.stitts.security.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    /** User zone home. */
    @GetMapping(value = "/account-profile")
    public Mono<String> userhome() {
        return Mono.just("/user/account-profile");
    }
    /** Administration zone home. */
    @GetMapping("/admin/account-profile")
    public Mono<String> adminhome() {
        return Mono.just("/admin/account-profile");
    }
    @GetMapping("/{email}")
    public Mono<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    // ... other endpoints related to User
}
