package com.jmt.jobs.service;

import com.jmt.jobs.entity.Job;
import com.jmt.jobs.model.JobImageDto;
import com.jmt.jobs.model.JobInfo;
import com.jmt.jobs.model.PolicyInfo;
import com.jmt.jobs.repository.JobImageRepository;
import com.jmt.jobs.repository.JobRepository;
import com.jmt.jobs.repository.PolicyItemRepository;
import com.jmt.jobs.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final JobImageRepository jobImageRepository;
    private final PolicyItemRepository policyItemRepository;
    private final PolicyRepository policyRepository;
    public Flux<Job> getJobsByUserId(Integer userId) {
        return jobRepository.findJobsById(userId);
    }
    public Flux<Job> get(int page, int size, String filter) {
        // Skipping (page * size) items and taking the next 'size' items from the Flux
        return jobRepository.findByFilter(filter)
                .skip((long) page * size)
                .take(size);
    }
    public Mono<JobInfo> getJobById(Integer jobId) {
        return jobRepository.findJobById(jobId)
                .flatMap(job -> Mono.zip(
                                policyRepository.findByCategory(job.getCategory())
                                        .flatMap(policy -> policyItemRepository.findByPolicyId(policy.getId())
                                                .collectList()
                                                .map(policyItems -> policyItems.stream()
                                                        .map(PolicyInfo::new)
                                                        .collect(Collectors.toList())
                                                )
                                        ),
                                jobImageRepository.findJobImagesById(jobId)
                                        .collectList()
                                        .defaultIfEmpty(Collections.emptyList()), // Default to empty list if no images
                                (policyInfos, jobImages) -> {
                                    List<JobImageDto> imageDtoList = jobImages.isEmpty() ? Collections.emptyList() // Skip if empty
                                            : jobImages.stream()
                                            .map(JobImageDto::new) // Create DTOs if images are present
                                            .collect(Collectors.toList());
                                    return new JobInfo(job, policyInfos, imageDtoList); // Create JobInfo with or without images
                                }
                        )
                )
                .switchIfEmpty(Mono.error(new Exception("Job not found")));
    }


    public Mono<Job> addImageByJobId(Integer jobId, String guid) {
        return jobImageRepository.insertImages(jobId, guid).then(jobRepository.findJobById(jobId));
    }


    public Mono<Job> removeImageById(String uri) {
        return jobImageRepository.findJobImagesByGUID(uri)
                .flatMap(jobImage ->
                        jobImageRepository.deleteImages(jobImage.getJobId(), jobImage.getId())
                                .thenReturn(jobImage)
                )
                .flatMap(jobImage -> jobRepository.findJobById(jobImage.getJobId()));
    }
}