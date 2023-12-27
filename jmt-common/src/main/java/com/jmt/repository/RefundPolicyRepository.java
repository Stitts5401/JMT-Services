package com.jmt.repository;

import com.jmt.entity.RefundPolicy;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RefundPolicyRepository extends ReactiveCrudRepository<RefundPolicy, Integer> {

    @Query("SELECT * FROM refund_policy WHERE job_id = :jobId")
    Flux<RefundPolicy> findByJobId(Integer jobId);
}
