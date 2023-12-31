package com.jmt.user.service;

import com.jmt.model.PasswordChangeRequest;

import reactor.core.publisher.Mono;

public interface AccountManagementService {
    Mono<Void> updatePassword(PasswordChangeRequest passwordChangeRequest);
    Mono<Void> updateEmail(String username, String email);
    Mono<Void> updatePhoneNumber(String username, String phoneNumber);
    Mono<Void> updateAddress(String username, String address);
    Mono<Void> updateFirstName(String username, String name);
    Mono<Void> updateLastName(String username, String name);
    Mono<Void> updateProfilePicture(String username, String profilePicture);
    Mono<Void> accountStatus(String username, boolean status);
}
