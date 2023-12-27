package com.jmt.repository;

import com.jmt.entity.ConfirmationPolicy;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ConfirmationPolicyRepository extends ReactiveCrudRepository<ConfirmationPolicy, Integer> {
    @Query("SELECT * FROM confirmation_policy WHERE job_id = :jobId")
    Flux<ConfirmationPolicy> findByJobId(Integer jobId);
}

