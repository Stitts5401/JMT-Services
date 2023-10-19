package com.stitts.security.repository;

import com.stitts.security.entity.Job;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface JobRepository extends ReactiveCrudRepository<Job, Long> {
    // ... any custom query methods
}
