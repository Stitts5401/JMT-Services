package com.jmt.jobs.service;

import com.jmt.jobs.entity.*;
import com.jmt.jobs.model.*;
import com.jmt.jobs.repository.*;
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
    private final CancellationPolicyRepository cancellationPolicyRepository;
    private final ConfirmationPolicyRepository confirmationPolicyRepository;
    private final RefundPolicyRepository refundPolicyRepository;
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
                                confirmationPolicyRepository.findByJobId(job.getId()).collectList(),
                                cancellationPolicyRepository.findByJobId(job.getId()).collectList(),
                                refundPolicyRepository.findByJobId(job.getId()).collectList(),
                                jobImageRepository.findJobImagesById(jobId).collectList().defaultIfEmpty(Collections.emptyList())
                        )
                        .map(tuple -> {
                            // Unpack the results from Mono.zip into respective lists
                            List<ConfirmationPolicy> confirmationPolicies = tuple.getT1();
                            List<CancellationPolicy> cancellationPolicies = tuple.getT2();
                            List<RefundPolicy> refundPolicies = tuple.getT3();
                            List<JobImage> jobImages = tuple.getT4();

                            // Convert policies and job images to their respective DTOs
                            List<ConfirmationPolicyInfo> confirmationPolicyInfos = confirmationPolicies.stream()
                                    .map(ConfirmationPolicyInfo::new)
                                    .collect(Collectors.toList());
                            List<CancellationPolicyInfo> cancellationPolicyInfos = cancellationPolicies.stream()
                                    .map(CancellationPolicyInfo::new)
                                    .collect(Collectors.toList());
                            List<RefundPolicyInfo> refundPolicyInfos = refundPolicies.stream()
                                    .map(RefundPolicyInfo::new)
                                    .collect(Collectors.toList());
                            List<JobImageDto> imageDtoList = jobImages.stream()
                                    .map(JobImageDto::new)
                                    .collect(Collectors.toList());

                            // Here we assume job has a getCategory method that returns the category name as a String
                            String category = job.getCategory();

                            // Create and return the JobInfo object with the policies
                            return new JobInfo(job, imageDtoList, category, confirmationPolicyInfos, cancellationPolicyInfos, refundPolicyInfos);
                        }))
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