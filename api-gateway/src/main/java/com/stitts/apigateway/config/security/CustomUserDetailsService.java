package com.stitts.apigateway.config.security;

import com.stitts.apigateway.entity.ReactiveUserDetails;
import com.stitts.apigateway.model.ChangePasswordRequest;
import com.stitts.apigateway.exception.UserNotFoundException;
import com.stitts.apigateway.repository.RoleRepository;
import com.stitts.apigateway.repository.UserRepository;
import com.stitts.apigateway.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return findUserWithRolesByEmail(username)
                .log()
                .map(user -> (UserDetails) user)  // Casting is fine if ReactiveUserDetails implements UserDetails
                .doOnNext(customUserDetails -> log.info("Fetched user details: {}", customUserDetails))
                .doOnError(throwable -> log.error("Error fetching user details: {}", throwable.getMessage()));
    }
    public Mono<ReactiveUserDetails> findUserByUsername(String email) {
        log.info("findByEmail: " + email);
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with email: " + email)));
    }
    public Mono<ReactiveUserDetails> saveUser(ReactiveUserDetails reactiveUserDetails) {
        return userRepository.save(reactiveUserDetails);
    }
    public Mono<ReactiveUserDetails> updateUser(ReactiveUserDetails reactiveUserDetails) {
        // you might need to check if the user exists before updating
        return userRepository.save(reactiveUserDetails);
    }
    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }
    public Mono<ReactiveUserDetails> findUserWithRolesByEmail(String email) {
        return findUserByUsername(email)
                .log()
                .flatMap(user -> roleRepository.findRolesByUserId(user.getId())
                        //TODO: Handle exception in UI
                        .switchIfEmpty(Mono.error(new IllegalStateException("No roles assigned to user")))
                        .flatMap( role -> {
                            user.setRole(role);
                            return Mono.just(user);
                        }).flatMap(user1 -> tokenRepository.findTokenByUserId(user.getId()))
                        //TODO: Handle exception in UI)
                        .flatMap( token -> {
                            if (token == null) {
                                return Mono.just(user);
                            }
                            user.setToken(token);
                            return Mono.just(user);
                        })
                                .onErrorResume( throwable -> {
                                    log.error("Error fetching user details: {}", throwable.getMessage());
                                    return Mono.error(throwable);
                                })
                .doOnNext(userObject -> log.info("User Service details: {}", userObject))
                );
    }
    public Mono<ReactiveUserDetails> findTokensByUserId(String email) {
        return null;
    }
    public Mono<Void> changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (ReactiveUserDetails) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return Mono.just(user)
                .flatMap(u -> {
                    // check if the current password is correct
                    if (!passwordEncoder.matches(request.getCurrentPassword(), u.getPassword())) {
                        return Mono.error(new IllegalStateException("Wrong password"));
                    }
                    // check if the two new passwords are the same
                    if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                        return Mono.error(new IllegalStateException("Passwords are not the same"));
                    }
                    // update the password
                    u.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    // save the new password
                    return userRepository.save(u);
                })
                .then();  // Convert to Mono<Void>
    }

}
