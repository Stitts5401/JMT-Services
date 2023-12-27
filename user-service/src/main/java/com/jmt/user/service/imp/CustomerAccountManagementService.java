package com.jmt.user.service.imp;

import com.jmt.entity.Job;
import com.jmt.model.JobInfo;
import com.jmt.model.PasswordChangeRequest;
import com.jmt.model.UserInfo;
import com.jmt.user.repository.UserRepository;
import com.jmt.user.service.AccountManagementService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
    public Mono<Void> updateEmail(String oldEmail, String newEmail) {
        return userRepository.findByEmail(oldEmail)
                .flatMap(user -> {
                    user.setEmail(newEmail);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updatePhoneNumber(String email, String phoneNumber) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    user.setPhoneNumber(phoneNumber);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updateAddress(String email, String address) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    user.setAddress(address);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updateFirstName(String email, String name) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    user.setFirstname(name);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updateLastName(String email, String name) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    user.setLastname(name);
                    return userRepository.save(user);
                }).then();
    }

    public Mono<Void> updateProfilePicture(String email, String profilePicture) {
        return userRepository.updateByEmail(email, profilePicture);
    }

    public Mono<Void> accountStatus(String email, boolean isEnabled) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    user.setEnabled(isEnabled);
                    return userRepository.save(user);
                }).then();
    }
    public Mono<UserInfo> getUserInformationFromEmail(String email ,  Collection<? extends GrantedAuthority> authorities) {
        return userRepository.findByEmail(email)
                .flatMap(user -> getJobs(user.getId())
                        .collectList()
                        .map(jobs -> new UserInfo(user, jobs, authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())))
                ).log();
    }
    private Flux<Job> getJobs(Integer userId) {
        return WebClient.create()
                .get()
                .uri(url+ "/jobs/user/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Job.class).log();
    }
}
