package com.jmt.user.controller;


import com.jmt.user.model.PasswordChangeRequest;
import com.jmt.user.model.UserInfo;
import com.jmt.user.service.imp.CustomerAccountManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserManagementController {

    private final CustomerAccountManagementService accountManagementService;

    @PostMapping("/management/update/password")
    private Mono<Void> updatePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        return accountManagementService.updatePassword(passwordChangeRequest);
    }

    @PostMapping("/management/update/email")
    private Mono<Void> updateEmail(@RequestBody String username, @RequestBody String email) {
        return accountManagementService.updateEmail(username, email);
    }

    @PostMapping("/management/update/phone-number")
    private Mono<Void> updatePhoneNumber(@RequestBody String username, @RequestBody String phoneNumber) {
        return accountManagementService.updatePhoneNumber(username, phoneNumber);
    }

    @PostMapping("/management/update/address")
    private Mono<Void> updateAddress(@RequestBody String username, @RequestBody String address) {
        return accountManagementService.updateAddress(username, address);
    }

    @PostMapping("/management/update/first-name")
    private Mono<Void> updateFirstName(@RequestBody String username, @RequestBody String name) {
        return accountManagementService.updateFirstName(username, name);
    }

    @PostMapping("/management/update/last-name")
    private Mono<Void> updateLastName(@RequestBody String username, @RequestBody String name) {
        return accountManagementService.updateLastName(username, name);
    }

    @PostMapping("/management/update/profile-picture")
    private Mono<Void> updateProfilePicture(@RequestBody String username, @RequestBody ByteBuffer profilePicture) {
        return accountManagementService.updateProfilePicture(username, profilePicture);
    }

    @PostMapping("/management/update/account-status")
    private Mono<Void> accountStatus(@RequestBody String username, @RequestBody boolean isEnabled) {
        return accountManagementService.accountStatus(username, isEnabled);
    }
    @GetMapping("/info")
    public Mono<UserInfo> userInfo(ServerHttpRequest request) {
        String email = request.getHeaders().getFirst("X-Preferred-Username");
        String authoritiesString = request.getHeaders().getFirst("X-Authorities");
        List<GrantedAuthority> authorities = Arrays.stream(authoritiesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return accountManagementService.getUserInformationFromEmail(email, authorities);
    }


}
