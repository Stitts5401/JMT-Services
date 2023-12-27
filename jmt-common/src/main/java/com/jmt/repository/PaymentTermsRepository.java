package com.jmt.repository;

import com.jmt.entity.PaymentTerms;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PaymentTermsRepository extends ReactiveCrudRepository<PaymentTerms, Integer> {
    @Query("SELECT * FROM payment_terms WHERE project_id = :projectId")
    Mono<PaymentTerms> findByProjectId(Integer projectId);
}
