package com.jmt.jobs;

import com.jmt.jobs.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication( scanBasePackages = {"com.jmt"})
@RequiredArgsConstructor
public class JobApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
}
