package com.jmt.webservice.controller;

import com.jmt.webservice.service.JobListingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping("/job")
@CircuitBreaker(name = "jobs", fallbackMethod = "fallbackMethod")
@RequiredArgsConstructor
public class JobController {

    private final JobListingService jobListingService;

    public String fallbackMethod(Exception e) {
        log.error("Fallback method triggered with exception {}", e.getMessage());
        return "fallback";
    }
    @GetMapping("/listings")
    public Mono<String> jobListings(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter,
            @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {

        // Retrieve jobs in a pageable fashion
        return oauthTokenMono
                .flatMap(auth -> jobListingService.retrieve(page, size, filter, auth))
                .doOnNext(jobPage -> {
                    model.addAttribute("JobsList", jobPage.toList() );
                })
                .thenReturn("jobs/listings");
    }
    @RequestMapping("/details/{jobId}")
    public Mono<String> jobDetails(Model model, @PathVariable("jobId") long jobId, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono){
        return oauthTokenMono
                .flatMap(auth -> jobListingService.retrieve(jobId, auth))
                .map(jobInfo -> {
                    model.addAttribute("job", jobInfo );
                    return "jobs/details";
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No Job found");
                    return Mono.just("redirect:/error");
                }));
    }
    @RequestMapping("/add")
    public Mono<String> addJob(Model model){

        //TODO Post job to job-service

        return Mono.just("jobs/listings");
    }
}





