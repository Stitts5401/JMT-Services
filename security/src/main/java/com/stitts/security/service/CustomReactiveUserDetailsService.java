package com.stitts.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.stitts.security.entity.CustomUserDetails;
@Service
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {
    private final UserService userService;
    public CustomReactiveUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findByEmail(username)
                .map(CustomUserDetails::new);
    }


}