package com.jmt.repository;

import com.jmt.entity.LaborRequirements;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LaborRequirementsRepository extends ReactiveCrudRepository<LaborRequirements, Integer> {
    @Query("SELECT * FROM labor_requirements WHERE project_id = :projectId")
    Flux<LaborRequirements> findByProjectId(Integer projectId);
}
