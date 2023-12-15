package com.jmt.jobs.service;

import com.jmt.jobs.entity.Job;
import com.jmt.jobs.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class JobService {

    private JobRepository jobRepository;
    public Flux<Job> getJobs(Integer userId) {
        return jobRepository.findJobsById(userId);
    }
}