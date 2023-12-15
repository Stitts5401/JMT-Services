package com.jmt.user.service.imp;

import com.jmt.user.entity.Job;
import com.jmt.user.model.PasswordChangeRequest;
import com.jmt.user.model.UserInfo;
import com.jmt.user.repository.UserRepository;
import com.jmt.user.service.AccountManagementService;
import io.r2dbc.spi.Blob;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerAccountManagementService implements AccountManagementService {

    @Value("${host.gateway}")
    private String url;

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
    public Mono<UserInfo> getUserInformationFromUsername(String email ,  Collection<? extends GrantedAuthority> authorities) {
        return userRepository.findByUsername(email)
                .flatMap(user -> getJobs(user.getId())
                        .collectList()
                        .map(jobs -> new UserInfo(user, jobs, authorities.stream()
                                .map(GrantedAuthority::getAuthority).collect(Collectors.toList())))
                );
    }
    private Flux<Job> getJobs(Integer userId) {
        return WebClient.create()
                .get()
                .uri(url+ "/jobs/user/" + userId)
                .retrieve()
                .bodyToFlux(Job.class);
    }
}
