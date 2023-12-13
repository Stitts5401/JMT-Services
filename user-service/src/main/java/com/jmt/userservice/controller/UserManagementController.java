package com.jmt.userservice.controller;


import com.jmt.userservice.model.PasswordChangeRequest;
import com.jmt.userservice.service.AccountManagementService;
import com.jmt.userservice.service.imp.CustomerAccountManagementService;
import io.r2dbc.spi.Blob;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
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
    private Mono<Void> updateProfilePicture(@RequestBody String username, @RequestBody Blob profilePicture) {
        return accountManagementService.updateProfilePicture(username, profilePicture);
    }

    @PostMapping("/management/update/account-status")
    private Mono<Void> accountStatus(@RequestBody String username, @RequestBody boolean isEnabled) {
        return accountManagementService.accountStatus(username, isEnabled);
    }

}
