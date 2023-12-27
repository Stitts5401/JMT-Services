package com.jmt.repository;

import com.jmt.entity.ComplianceRegulations;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ComplianceRegulationsRepository extends ReactiveCrudRepository<ComplianceRegulations, Integer> {
    @Query("SELECT * FROM compliance_regulations WHERE project_id = :projectId")
    Mono<ComplianceRegulations> findByProjectId(Integer projectId);
}
