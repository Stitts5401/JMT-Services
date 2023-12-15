package com.jmt.jobs.controller;

import com.jmt.jobs.entity.Job;
import com.jmt.jobs.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/user/{userId}")
    public Flux<Job> getJobsByUser(@PathVariable Integer userId) {
        return jobService.getJobs(userId);
    }
}