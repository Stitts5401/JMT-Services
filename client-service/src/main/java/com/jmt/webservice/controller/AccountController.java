package com.jmt.webservice.controller;

import com.jmt.webservice.literal.NationalityData;
import com.jmt.model.UserInfo;
import com.jmt.webservice.service.AccountService;
import com.jmt.webservice.service.GoogleCloudStorageService;
import com.jmt.webservice.service.UserInfoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@CircuitBreaker(name = "account", fallbackMethod = "fallback")
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final UserInfoService userInfoService;
    private final AccountService accountService;

    @GetMapping("/info")
    public Mono<String> getUserAccountInfo(Model model, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {
        return oauthTokenMono
                .flatMap(userInfoService::retrieveUserInfo).map( userInfo -> {
                    // Add user info attributes only if userInfo is not null
                        boolean isComplete =
                                userInfo.getFirstname() != null && userInfo.getLastname() != null &&
                                        userInfo.getEmail() != null && userInfo.getAddress() != null;
                        int percentComplete = 0;
                        if (isComplete) percentComplete += 50;

                        model.addAttribute("isComplete", isComplete);
                        model.addAttribute("percentComplete", percentComplete);
                        model.addAttribute("nationalities", NationalityData.getCommonNationalities());
                        model.addAttribute("userInfo", userInfo);
                    return "account/info";
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No AuthenticationPrincipal found, redirecting to Keycloak");
                    return Mono.just("redirect:/oauth2/authorization/keycloak");
                }))
                .doOnNext(viewName -> log.info("Rendering view: " + viewName)) // defer the rendering until the userInfo Mono completes
                .onErrorResume(ex -> Mono.just("redirect:/logout"));
    }
    @RequestMapping("/jobs")
    public Mono<String> getMyJobs(Model model, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono){
        return oauthTokenMono
                .flatMap(userInfoService::retrieveUserInfo).map( userInfo -> {
                    model.addAttribute("userInfo", userInfo);
                    model.addAttribute("jobsList", userInfo.getJobs() );
                    return "account/jobs";
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No AuthenticationPrincipal found, redirecting to Keycloak");
                    return Mono.just("redirect:/oauth2/authorization/keycloak");
                }))
                .doOnNext(viewName -> log.info("Rendering view: " + viewName)) // defer the rendering until the userInfo Mono completes
                .onErrorResume(ex -> Mono.just("redirect:/logout"));
    }
    @RequestMapping("/delete")
    public Mono<String> deleteAccount(Model model, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono){
        return oauthTokenMono
                .flatMap(userInfoService::retrieveUserInfo).map( userInfo -> {
                    model.addAttribute("userInfo", userInfo);
                    return "account/delete";
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No AuthenticationPrincipal found, redirecting to Keycloak");
                    return Mono.just("redirect:/oauth2/authorization/keycloak");
                }))
                .doOnNext(viewName -> log.info("Rendering view: " + viewName)) // defer the rendering until the userInfo Mono completes
                .onErrorResume(ex -> Mono.just("redirect:/logout"));
    }
    @RequestMapping("/settings")
    public Mono<String> accountSettings(Model model, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono){
        return oauthTokenMono
                .flatMap(userInfoService::retrieveUserInfo).map( userInfo -> {
                    model.addAttribute("userInfo", userInfo);
                    return "account/settings";
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No AuthenticationPrincipal found, redirecting to Keycloak");
                    return Mono.just("redirect:/oauth2/authorization/keycloak");
                }))
                .doOnNext(viewName -> log.info("Rendering view: " + viewName)) // defer the rendering until the userInfo Mono completes
                .onErrorResume(ex -> Mono.just("redirect:/logout"));
    }
    @RequestMapping("/logout")
    public Mono<String> logout( @AuthenticationPrincipal OAuth2AuthenticationToken oauthToken, WebSession session ) {

        oauthToken.setAuthenticated(false);
        return session.invalidate()
                .then( Mono.just("home") );
    }
    @PostMapping("/update-account")
    private Mono<Void> updateAccount(@RequestBody UserInfo userInfo, Model model, @AuthenticationPrincipal OAuth2AuthenticationToken oauthToken) {
        return userInfoService.retrieveUserInfo(oauthToken)
                .log()
                .flatMap(dbInfo -> {
                    Errors errors = new BeanPropertyBindingResult(userInfo, "userInfo");
                    if (errors.hasErrors()) {
                        model.addAttribute("hasError", true);
                        model.addAttribute("errorMsg", errors.getAllErrors().get(0).getDefaultMessage());
                        return Mono.error(new ValidationException(String.valueOf(errors))); // Custom exception
                    }

                    Mono<Void> emailUpdateMono = dbInfo.getEmail().equals(userInfo.getEmail()) ?
                            Mono.empty() :
                            accountService.updateEmail(oauthToken, userInfo.getEmail());

                    Mono<Void> phoneUpdateMono = dbInfo.getPhoneNumber().equals(userInfo.getPhoneNumber()) ?
                            Mono.empty() :
                            accountService.updatePhoneNumber(oauthToken, userInfo.getPhoneNumber());

                    Mono<Void> addressUpdateMono = dbInfo.getAddress().equals(userInfo.getAddress()) ?
                            Mono.empty() :
                            accountService.updateAddress(oauthToken, userInfo.getAddress());

                    Mono<Void> firstNameUpdateMono = dbInfo.getFirstname().equals(userInfo.getFirstname()) ?
                            Mono.empty() :
                            accountService.updateFirstName(oauthToken, userInfo.getFirstname());

                    Mono<Void> lastNameUpdateMono = dbInfo.getLastname().equals(userInfo.getLastname()) ?
                            Mono.empty() :
                            accountService.updateLastName(oauthToken, userInfo.getLastname());

                    model.addAttribute("successMessage", "Account updated successfully!");
                    return Mono.when(emailUpdateMono, phoneUpdateMono, addressUpdateMono, firstNameUpdateMono, lastNameUpdateMono);
                });
    }


}