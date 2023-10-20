package com.stitts.security.controller.roles;

import com.stitts.security.entity.User;
import com.stitts.security.service.UserService;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping(value = "/account-profile")
    public Mono<String> userHome() {
        return Mono.just("/user/account-profile");
    }
    @GetMapping("/{email}")
    public Mono<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

}
