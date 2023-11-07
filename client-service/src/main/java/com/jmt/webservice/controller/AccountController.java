package com.jmt.webservice.controller;

import com.jmt.webservice.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final UserInfoService userInfoService;
    @GetMapping("/info")
    public Mono<String> getUserAccountInfo(Model model, @AuthenticationPrincipal OAuth2AuthenticationToken oauthToken) {

        if (oauthToken == null) {
            return Mono.just("redirect:/oauth2/authorization/keycloak");
        }

        return userInfoService.retrieveUserInfo(oauthToken)
                .log()
                .doOnNext(userInfo -> {
                    // Add user info attributes only if userInfo is not null
                    if (userInfo != null) {
                        boolean verifiedEmail = Boolean.TRUE.equals(oauthToken.getPrincipal().getAttribute("email_verified"));
                        boolean isComplete =
                                userInfo.getFirstname() != null && userInfo.getLastname() != null &&
                                userInfo.getEmail() != null && userInfo.getAddress() != null;
                        int percentComplete = 0;
                        if (verifiedEmail) percentComplete += 50;
                        if (isComplete) percentComplete += 50;
                        model.addAttribute("firstname", userInfo.getFirstname());
                        model.addAttribute("lastname", userInfo.getLastname());
                        model.addAttribute("email", userInfo.getEmail());
                        model.addAttribute("phone", userInfo.getEmail());
                        model.addAttribute("emailVerified", verifiedEmail);
                        model.addAttribute("isComplete", isComplete);
                        model.addAttribute("percentComplete", percentComplete);
                        model.addAttribute("isAdmin", userInfo.getRoles().stream().anyMatch(role -> role.contains("admin")));
                        model.addAttribute("isUser", userInfo.getRoles().stream().anyMatch(role -> role.contains("user")));
                        model.addAttribute("jobs", userInfo.getJobs());
                    }
                })
                .then(Mono.fromCallable(() -> "account/info")) // defer the rendering until the userInfo Mono completes
                .onErrorResume(ex -> Mono.just("redirect:/logout"));
    }

    @GetMapping("/logout")
    public Mono<String> logout() {

        return Mono.just("home");
    }
    @GetMapping("/job-listings")
    public Mono<String> getJobListings(Model model){
        return Mono.just("job-listings");
    }
}