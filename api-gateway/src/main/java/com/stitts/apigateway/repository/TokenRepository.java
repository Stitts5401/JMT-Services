package com.stitts.apigateway.repository;


import com.stitts.apigateway.entity.Token;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TokenRepository extends ReactiveCrudRepository<Token, Long> {
  @Query("SELECT * FROM token WHERE user_id = :id")
  Mono<Token> findTokenByUserId(@Param("id") Integer id);
}
