package com.jmt.webservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if ("dev".equals(activeProfile)) {
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/")
                    .setCacheControl(CacheControl.noCache());
        } else {
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/")
                    .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
        }
    }
}