package com.stitts.apigateway.repository;

import com.stitts.apigateway.config.security.ReactiveUserDetails;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<ReactiveUserDetails, Long> {

    @Query("SELECT u.*, r.* FROM users u " +
            "LEFT JOIN users_roles ur ON u.user_id = ur.user_id " +
            "LEFT JOIN roles r ON ur.role_id = r.role_id " +
            "WHERE u.email = :email")
    Mono<ReactiveUserDetails> findByEmailWithRoles(@Param("email") String email);

    @Query("SELECT r.name FROM users_roles ur JOIN roles r ON ur.role_id = r.role_id WHERE ur.user_id = :userId")
    Flux<String> findRoleNamesByUserId(@Param("userId") Long userId);

    Mono<ReactiveUserDetails> findByEmail(String username);
}
