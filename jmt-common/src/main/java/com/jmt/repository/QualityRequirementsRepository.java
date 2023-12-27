package com.jmt.repository;

import com.jmt.entity.QualityRequirements;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface QualityRequirementsRepository extends ReactiveCrudRepository<QualityRequirements, Integer> {
    @Query("SELECT * FROM quality_requirements WHERE project_id = :projectId")
    Mono<QualityRequirements> findByProjectId(Integer projectId);
}
