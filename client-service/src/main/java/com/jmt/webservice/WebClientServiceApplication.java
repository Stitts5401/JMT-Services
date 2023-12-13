package com.jmt.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WebClientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebClientServiceApplication.class, args);
    }

}
