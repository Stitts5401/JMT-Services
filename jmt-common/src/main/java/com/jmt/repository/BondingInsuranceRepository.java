package com.jmt.repository;

import com.jmt.entity.BondingInsurance;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BondingInsuranceRepository extends ReactiveCrudRepository<BondingInsurance, Integer> {
    @Query("SELECT * FROM bonding_insurance WHERE project_id = :projectId")
    Mono<BondingInsurance> findByProjectId(Integer projectId);
}


