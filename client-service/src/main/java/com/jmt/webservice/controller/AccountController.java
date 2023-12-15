package com.jmt.webservice.controller;

import com.jmt.webservice.literal.NationalityData;
import com.jmt.webservice.model.UserInfo;
import com.jmt.webservice.service.AccountService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

                        model.addAttribute("emailVerified", verifiedEmail);
                        model.addAttribute("isComplete", isComplete);
                        model.addAttribute("percentComplete", percentComplete);
                        model.addAttribute("nationalities", NationalityData.getCommonNationalities());
                        model.addAttribute("userInfo", userInfo);
                    }
                })
                .then(Mono.fromCallable(() -> "account/info")) // defer the rendering until the userInfo Mono completes
                .onErrorResume(ex -> Mono.just("redirect:/logout"));
    }
    @GetMapping("/job-listings")
    public Mono<String> getJobListings(Model model){
        return Mono.just("job-listings");
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
                    userInfo.validate(userInfo, errors);

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