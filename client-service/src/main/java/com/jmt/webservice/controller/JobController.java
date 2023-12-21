package com.jmt.webservice.controller;

import com.jmt.webservice.service.AccountService;
import com.jmt.webservice.service.JobListingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping("/job")
@CircuitBreaker(name = "jobs", fallbackMethod = "fallbackMethod")
@RequiredArgsConstructor
public class JobController {

    private final JobListingService jobListingService;
    private final AccountService accountService;

    public String fallbackMethod(Exception e) {
        log.error("Fallback method triggered with exception {}", e.getMessage());
        return "fallback";
    }
    @GetMapping("/listings")
    public Mono<String> jobListings(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String filter, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {

        // Retrieve jobs in a pageable fashion
        return oauthTokenMono
                .flatMap(auth -> jobListingService.retrieve(page, size, filter, auth))
                .doOnNext(jobPage -> {
                    model.addAttribute("JobsList", jobPage.getContent() );
                })
                .thenReturn("jobs/listings");
    }
    @RequestMapping("/details/{jobId}")
    public Mono<String> jobDetails(Model model, @PathVariable("jobId") long jobId, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono){
        return oauthTokenMono
                .flatMap(auth -> jobListingService.retrieve(jobId, auth))
                .map(jobInfo -> {
                    model.addAttribute("job", jobInfo );
                    model.addAttribute("policies", jobInfo.getPolicyInfo() );
                    return "jobs/details";
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No Job found");
                    return Mono.just("redirect:/error");
                }));
    }
    @RequestMapping("/edit/{jobId}")
    public Mono<String> jobEdit(Model model, @PathVariable("jobId") long jobId, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono){
        return oauthTokenMono
                .flatMap(auth -> jobListingService.retrieve(jobId, auth))
                .map(jobInfo -> {
                    model.addAttribute("job", jobInfo );
                    model.addAttribute("policies", jobInfo.getPolicyInfo() );
                    return "jobs/edit";
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No Job found");
                    return Mono.just("redirect:/error");
                }));
    }
}





