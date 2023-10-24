package main;

import com.jmt.webservice.WebServiceApplication;
import com.jmt.webservice.model.LoginForm;
import com.jmt.webservice.service.WebService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = WebServiceApplication.class)
@SpringBootTest
public class LoginControllerTest  {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WebService webService;

    @Test
    public void testLoginUser() {
        LoginForm sampleLoginForm = new LoginForm();
        // ... populate sampleLoginForm as needed ...

        when(webService.authenticateAndRedirect(any(LoginForm.class), any(), any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/verify-login")
                .bodyValue(sampleLoginForm)
                .exchange()
                .expectStatus().isOk();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public WebTestClient webTestClient() {
            return WebTestClient.bindToServer().baseUrl("http://localhost:8050").build();
        }
    }
}
