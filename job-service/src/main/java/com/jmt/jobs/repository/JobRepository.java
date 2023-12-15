package com.jmt.jobs.repository;



import com.jmt.jobs.entity.Job;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface JobRepository extends ReactiveCrudRepository<Job, Long>{
    @Query("SELECT * FROM job WHERE user_id = :user_id")
    Flux<Job> findJobsById(Integer user_id);
}
