package org.example.gatewayservice.tokenVerification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "customWebClientBuilder")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
