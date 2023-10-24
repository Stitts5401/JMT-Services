package com.stitts.apigateway.token;


import com.stitts.apigateway.entity.Token;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TokenRepository extends ReactiveCrudRepository<Token, Long> {

  // Adjusted the SQL to be a generic example. Make sure you replace it with valid SQL for your database
  @Query("SELECT * FROM token t INNER JOIN user u ON t.user_id = u.id WHERE u.id = :id AND (t.expired = false OR t.revoked = false)")
  Flux<Token> findAllValidTokenByUser(@Param("id") Integer id);
  Mono<Token> findByToken(String token);
}

