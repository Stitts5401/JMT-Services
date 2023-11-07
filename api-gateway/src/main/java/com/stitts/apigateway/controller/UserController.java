package com.stitts.apigateway.controller;

import com.stitts.apigateway.model.UserInfo;
import com.stitts.apigateway.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserDetailService service;

    @GetMapping("/user/info")
    @PreAuthorize("hasRole('ROLE_user')")
    public Mono<UserInfo> userInfo(@AuthenticationPrincipal Principal principal) {
        Authentication authentication = (Authentication) principal;
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        String username = jwtAuth.getToken().getClaimAsString("preferred_username");
        Collection<GrantedAuthority> authorities = jwtAuth.getAuthorities();

        return Mono.just(
                service.getUserInformationFromUsername(username, authorities)
        ).flatMap(userInfo -> userInfo);
    }


}
