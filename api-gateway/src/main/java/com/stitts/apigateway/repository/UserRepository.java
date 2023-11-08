package com.stitts.apigateway.repository;

import com.stitts.apigateway.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("SELECT * FROM user WHERE prefered_username = :preferred_username")
    Mono<User> findByUsername(String preferred_username);
}
