    package com.stitts.security.controller;

    import com.stitts.security.exceptions.UserNotFoundException;
    import com.stitts.security.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.ReactiveAuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.context.ReactiveSecurityContextHolder;
    import org.springframework.security.core.context.SecurityContext;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;
    import reactor.core.publisher.Mono;
    import reactor.core.scheduler.Schedulers;

    @Controller
    public class LoginController {

        private final ReactiveAuthenticationManager reactiveAuthenticationManager;

        public LoginController(ReactiveAuthenticationManager reactiveAuthenticationManager) {
            this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        }
        @GetMapping("/login.html")
        public Mono<String> login() {
            return Mono.just("login");
        }

        @GetMapping("/login-error.html")
        public Mono<String> loginError(Model model) {
            model.addAttribute("loginError", Boolean.TRUE);
            return Mono.just("login-error");
        }

        // Handle form submission
        @PostMapping("/login")
        public Mono<String> loginUser(@RequestParam String username, @RequestParam String password, Model model, RedirectAttributes redirectAttributes) {
            return Mono.just(new UsernamePasswordAuthenticationToken(username, password))
                    .flatMap(authentication -> reactiveAuthenticationManager.authenticate(authentication)
                            .publishOn(Schedulers.boundedElastic())
                            .doOnSuccess(authenticated -> {
                                SecurityContext securityContext = ReactiveSecurityContextHolder.getContext().block();
                                securityContext.setAuthentication(authenticated);
                            })
                            .thenReturn(authentication)
                    )
                    .flatMap(authentication -> {
                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                        // Perform any role-based logic here
                        // For example, you can use userDetails.getUsername() to retrieve the username
                        return Mono.just("redirect:/users/account-profile"); // Redirect to a dashboard or any authenticated page
                    })
                    .onErrorResume(AuthenticationException.class, ex -> {
                        redirectAttributes.addFlashAttribute("loginError", true);
                        return Mono.just("redirect:/login");
                    })
                    .onErrorResume(UserNotFoundException.class, ex -> {
                        redirectAttributes.addFlashAttribute("notFound", true);
                        return Mono.just("redirect:/login");
                    });
            }
        }

