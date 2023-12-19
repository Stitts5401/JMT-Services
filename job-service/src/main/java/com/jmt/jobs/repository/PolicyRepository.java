package com.jmt.jobs.repository;

import com.jmt.jobs.entity.Policy;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PolicyRepository extends ReactiveCrudRepository<Policy, Integer> {
    @Query("SELECT * FROM policies WHERE category = :category")
    Mono<Policy> findByCategory(String category);
    // Custom query methods if needed
}

