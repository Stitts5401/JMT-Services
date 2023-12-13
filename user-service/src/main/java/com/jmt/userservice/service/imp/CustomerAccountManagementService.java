package com.jmt.userservice.service.imp;

import com.jmt.userservice.model.PasswordChangeRequest;
import com.jmt.userservice.repository.UserRepository;
import com.jmt.userservice.service.AccountManagementService;
import io.r2dbc.spi.Blob;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class CustomerAccountManagementService implements AccountManagementService {

    private final UserRepository userRepository;

    @Value("${keycloak.password-change.url}")
    private String KEYCLOACK_PASSWORD_CHANGE_URL;
    public Mono<Void> updatePassword(PasswordChangeRequest passwordChangeRequest) {
        return WebClient.create()
                .post()
                .uri(KEYCLOACK_PASSWORD_CHANGE_URL)
                .body(Mono.just(passwordChangeRequest), String.class)
                .retrieve()
                .bodyToMono(Void.class);
    }
    public Mono<Void> updateEmail(String username, String email) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    user.setEmail(email);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updatePhoneNumber(String username, String phoneNumber) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    user.setPhoneNumber(phoneNumber);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updateAddress(String username, String address) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    user.setAddress(address);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updateFirstName(String username, String name) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    user.setFirstname(name);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updateLastName(String username, String name) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    user.setLastname(name);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updateProfilePicture(String username, Blob profilePicture) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    user.setProfilePicture(profilePicture);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> accountStatus(String username, boolean isEnabled) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    user.setEnabled(isEnabled);
                    return userRepository.save(user);
                }).then();
    }
}
