package com.jmt.repository;

import com.jmt.entity.WarrantyInformation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WarrantyInformationRepository extends ReactiveCrudRepository<WarrantyInformation, Integer> {
    @Query("SELECT * FROM warranty_information WHERE project_id = :projectId")
    Mono<WarrantyInformation> findByProjectId(Integer projectId);
}
