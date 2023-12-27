package com.jmt.repository;


import com.jmt.entity.CancellationPolicy;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CancellationPolicyRepository extends ReactiveCrudRepository<CancellationPolicy, Integer> {
    @Query("SELECT * FROM cancellation_policy WHERE job_id = :jobId")
    Flux<CancellationPolicy> findByJobId(Integer jobId);
}
