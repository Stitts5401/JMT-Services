package com.stitts.apigateway.service.Imp;

import com.stitts.apigateway.entity.CustomUserDetails;
import com.stitts.apigateway.entity.dto.User;
import com.stitts.apigateway.exception.UserNotFoundException;
import com.stitts.apigateway.repository.RoleRepository;
import com.stitts.apigateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return findUserWithRolesByEmail(username)
                .map(user -> (UserDetails) new CustomUserDetails(user))
                .doOnNext(customUserDetails -> log.info("Fetched user details: {}", customUserDetails));
    }
    public Mono<User> findByEmail(String email) {
        log.info("findByEmail: " + email);
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with email: " + email)));
    }

    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> updateUser(User user) {
        // you might need to check if the user exists before updating
        return userRepository.save(user);
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    public Mono<User> findUserWithRolesByEmail(String email) {
        return findByEmail(email)
                .flatMap(user -> {
                    Mono<User> userMono = roleRepository.findRolesByUserId(user.getId())
                            .collectList()
                            .doOnNext(user::setRoles)
                            .thenReturn(user);
                    log.info("User Service details: " + user.toString());
                    return userMono;
                });
    }
}
