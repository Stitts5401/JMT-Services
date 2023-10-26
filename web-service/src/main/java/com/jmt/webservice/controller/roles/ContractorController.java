package com.jmt.webservice.controller.roles;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/contractor")
@PreAuthorize("hasRole('CONTRACTOR')")
public class ContractorController {
    @GetMapping(value = "/account-profile")
    public Mono<String> contractorHome() {
        return Mono.just("contractor/account-profile");
    }
}