package com.jmt.jobs.repository;

import com.jmt.jobs.entity.PolicyItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PolicyItemRepository extends ReactiveCrudRepository<PolicyItem, Integer> {
    @Query("SELECT * FROM policy_items WHERE policy_id = :id")
    Flux<PolicyItem> findByPolicyId(Integer id);
    // Custom query methods if needed
}
