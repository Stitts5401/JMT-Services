package com.jmt.repository;

import com.jmt.entity.BudgetConstraints;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BudgetConstraintsRepository extends ReactiveCrudRepository<BudgetConstraints, Integer> {
    @Query("SELECT * FROM budget_constraints WHERE project_id = :projectId")
    Mono<BudgetConstraints> findByProjectId(Integer projectId);
}
