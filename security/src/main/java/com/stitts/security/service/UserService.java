package com.stitts.security.service;

import com.stitts.security.entity.Role;
import com.stitts.security.entity.User;
import com.stitts.security.exceptions.UserNotFoundException;
import com.stitts.security.repository.RoleRepository;
import com.stitts.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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
                    log.info("User Service details: "+ user.toString());
                    return userMono;
                });
    }

}
