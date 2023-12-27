package com.jmt.repository;

import com.jmt.entity.MaterialsEquipment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MaterialsEquipmentRepository extends ReactiveCrudRepository<MaterialsEquipment, Integer> {
    @Query("SELECT * FROM materials_equipment WHERE project_id = :projectId")
    Flux<MaterialsEquipment> findByProjectId(Integer projectId);
}
