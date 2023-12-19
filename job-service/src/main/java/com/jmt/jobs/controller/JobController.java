package com.jmt.jobs.controller;

import com.jmt.jobs.entity.Job;
import com.jmt.jobs.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/user/{userId}")
    public Flux<Job> getJobsByUser(@PathVariable Integer userId) {
        return jobService.get(userId);
    }
    @GetMapping("/details/{jobId}")
    public Mono<Job> getJobsByUser(@PathVariable long jobId) {
        return jobService.get(jobId);
    }
    @GetMapping("/listings")
    public Mono<String> jobListings(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {
        // Apply pagination and filtering in the service layer
        return oauthTokenMono
                .flatMapMany(auth -> jobService.get(page, size, filter))
                .collectList()
                .doOnNext(jobs -> model.addAttribute("jobs", jobs))
                .thenReturn("jobs/listings");
    }
}