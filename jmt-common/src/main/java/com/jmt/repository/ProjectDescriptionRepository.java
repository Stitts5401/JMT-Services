package com.jmt.repository;

import com.jmt.entity.ProjectDescription;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProjectDescriptionRepository extends ReactiveCrudRepository<ProjectDescription, Integer> {
    @Query("SELECT * FROM project_description WHERE id = :projectId")
    Mono<ProjectDescription> findByProjectId(Integer projectId);
}
