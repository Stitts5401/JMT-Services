package com.jmt.repository;

import com.jmt.entity.SiteConditions;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SiteConditionsRepository extends ReactiveCrudRepository<SiteConditions, Integer> {
    @Query("SELECT * FROM site_conditions WHERE project_id = :projectId")
    Mono<SiteConditions> findByProjectId(Integer projectId);
}
