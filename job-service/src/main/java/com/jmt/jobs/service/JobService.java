package com.jmt.jobs.service;

import com.jmt.jobs.entity.Job;
import com.jmt.jobs.model.JobInfo;
import com.jmt.jobs.model.PolicyInfo;
import com.jmt.jobs.repository.JobRepository;
import com.jmt.jobs.repository.PolicyItemRepository;
import com.jmt.jobs.repository.PolicyRepository;
import io.r2dbc.spi.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final PolicyItemRepository policyItemRepository;
    private final PolicyRepository policyRepository;
    public Flux<Job> get(Integer userId) {
        return jobRepository.findJobsById(userId);
    }
    public Flux<Job> get(int page, int size, String filter) {
        // Skipping (page * size) items and taking the next 'size' items from the Flux
        return jobRepository.findByFilter(filter)
                .skip((long) page * size)
                .take(size);
    }
    public Mono<JobInfo> get(Long jobId) {
        return jobRepository.findById(jobId)
                .flatMap(job -> policyRepository.findByCategory(job.getCategory())
                        .flatMap(policy -> policyItemRepository.findByPolicyId(policy.getId())
                                .collectList() // Collect Flux<PolicyItem> into a List<PolicyItem>
                                .map(policyItems -> {
                                    // Convert each PolicyItem to PolicyInfo
                                    List<PolicyInfo> policyInfos = policyItems.stream()
                                            .map(PolicyInfo::new)
                                            .collect(Collectors.toList());
                                    // Create JobInfo object
                                    return new JobInfo(job, policyInfos);
                                })
                        )
                )
                .switchIfEmpty(Mono.error(new Exception("Job not found")));
    }

}