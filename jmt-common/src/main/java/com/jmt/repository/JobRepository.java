package com.jmt.repository;



import com.jmt.entity.Job;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface JobRepository extends ReactiveCrudRepository<Job, Long>{
    @Query("SELECT * FROM job WHERE user_id = :user_id")
    Flux<Job> findJobsById(Integer user_id);
    @Query("SELECT * FROM job WHERE id = :job_id")
    Mono<Job> findJobById(Integer job_id);
    @Query("SELECT * FROM job WHERE name LIKE :filter")
    Flux<Job> findByFilter(@Param("filter") String filter);
}
