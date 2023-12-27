package com.jmt.repository;



import com.jmt.entity.JobImage;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface JobImageRepository extends ReactiveCrudRepository<JobImage, Long>{
    @Query("SELECT * FROM job_images WHERE job_id = :job_id")
    Flux<JobImage> findJobImagesById(Integer job_id);
    @Query("SELECT * FROM job_images WHERE guid = :guid")
    Mono<JobImage> findJobImagesByGUID(String guid);
    @Query("INSERT INTO job_images (job_id, guid) VALUES (:job_id, :guid)")
    Mono<Void> insertImages(Integer job_id, String guid);
    @Query("DELETE FROM job_images WHERE job_id = :job_id AND id = :job_image_id")
    Mono<Void> deleteImages(Integer job_id, Integer job_image_id);
}
