package com.jmt.user.repository;

import com.jmt.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("SELECT * FROM user WHERE email = :email")
    Mono<User> findByEmail(String email);

    @Query("UPDATE user SET profile_picture = :blobName WHERE email = :email")
    Mono<Void> updateByEmail(String email, String blobName);
}
