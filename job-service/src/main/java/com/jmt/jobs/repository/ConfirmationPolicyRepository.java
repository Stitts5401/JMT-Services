package com.jmt.jobs.repository;

import com.jmt.jobs.entity.CancellationPolicy;
import com.jmt.jobs.entity.ConfirmationPolicy;
import com.jmt.jobs.entity.RefundPolicy;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ConfirmationPolicyRepository extends ReactiveCrudRepository<ConfirmationPolicy, Integer> {
    @Query("SELECT * FROM confirmation_policy WHERE job_id = :jobId")
    Flux<ConfirmationPolicy> findByJobId(Integer jobId);
}

