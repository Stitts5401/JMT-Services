package com.jmt.repository;

import com.jmt.entity.LocationDetails;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LocationDetailsRepository extends ReactiveCrudRepository<LocationDetails, Integer> {
    @Query("SELECT * FROM location_details WHERE project_id = :projectId")
    Mono<LocationDetails> findByProjectId(Integer projectId);
}
