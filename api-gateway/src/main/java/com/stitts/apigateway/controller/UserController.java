package com.stitts.apigateway.controller;

import com.stitts.apigateway.exception.UserNotFoundException;
import com.stitts.apigateway.model.ChangePasswordRequest;
import com.stitts.apigateway.config.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CustomUserDetailsService service;
    @PatchMapping
    public Mono<ResponseEntity<Object>> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        return service.changePassword(request, connectedUser)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> {
                    if (e instanceof UserNotFoundException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

}
