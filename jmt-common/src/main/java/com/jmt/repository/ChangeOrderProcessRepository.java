package com.jmt.repository;

import com.jmt.entity.ChangeOrderProcess;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChangeOrderProcessRepository extends ReactiveCrudRepository<ChangeOrderProcess, Integer> {
    @Query("SELECT * FROM change_order_process WHERE project_id = :projectId")
    Mono<ChangeOrderProcess> findByProjectId(Integer projectId);
}
