package com.jmt.userservice.repository;

import com.jmt.userservice.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("SELECT * FROM user WHERE prefered_username = :preferred_username")
    Mono<User> findByUsername(String preferred_username);
}
