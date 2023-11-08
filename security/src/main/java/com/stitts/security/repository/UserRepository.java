package com.stitts.security.repository;

import com.stitts.security.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    @Query("SELECT * FROM users WHERE email = :email")
    Mono<User> findByEmail(@Param("email") String email);

    @Query("SELECT u.*, r.* FROM users u " +
            "LEFT JOIN users_roles ur ON u.user_id = ur.user_id " +
            "LEFT JOIN roles r ON ur.role_id = r.role_id " +
            "WHERE u.email = :email")
    Mono<User> findByEmailWithRoles(@Param("email") String email);

    @Query("SELECT r.name FROM users_roles ur JOIN roles r ON ur.role_id = r.role_id WHERE ur.user_id = :userId")
    Flux<String> findRoleNamesByUserId(@Param("userId") Long userId);

}
