package com.stitts.apigateway.repository;

import com.stitts.apigateway.entity.ReactiveUserDetails;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<ReactiveUserDetails, Long> {

    @Query("SELECT * FROM user WHERE email = :email")
    Mono<ReactiveUserDetails> findByEmail(String email);
}
