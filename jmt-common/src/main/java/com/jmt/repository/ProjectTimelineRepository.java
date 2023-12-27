package com.jmt.repository;

import com.jmt.entity.ProjectTimeline;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProjectTimelineRepository extends ReactiveCrudRepository<ProjectTimeline, Integer> {
    @Query("SELECT * FROM project_timeline WHERE project_id = :projectId")
    Mono<ProjectTimeline> findByProjectId(Integer projectId);
}
