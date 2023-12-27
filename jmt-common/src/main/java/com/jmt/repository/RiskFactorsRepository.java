package com.jmt.repository;

import com.jmt.entity.RiskFactors;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RiskFactorsRepository extends ReactiveCrudRepository<RiskFactors, Integer> {
    @Query("SELECT * FROM risk_factors WHERE project_id = :projectId")
    Mono<RiskFactors> findByProjectId(Integer projectId);
}
