package com.jmt.repository;

import com.jmt.entity.WorkBreakdown;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WorkBreakdownRepository extends ReactiveCrudRepository<WorkBreakdown, Integer> {
    @Query("SELECT * FROM work_breakdown WHERE project_id = :projectId")
    Flux<WorkBreakdown> findByProjectId(Integer projectId);
}
