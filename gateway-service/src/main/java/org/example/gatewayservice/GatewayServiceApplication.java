package org.example.gatewayservice;

import org.example.gatewayservice.tokenVerification.TokenVerificationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Configuration
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Bean
    public DiscoveryClientRouteDefinitionLocator dynamicRoute(
            ReactiveDiscoveryClient rdc,
            DiscoveryLocatorProperties dlp)
    {
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }


    @Bean
    public GlobalFilter logResponseFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                exchange.getResponse().getHeaders().forEach((key, value) -> {
                    System.out.println(key + ": " + value);
                });
            }));
        };
    }


    @Bean
    public GlobalFilter tokenVerificationFilter(@Qualifier("customWebClientBuilder") WebClient.Builder webClientBuilder) {
        return new TokenVerificationFilter(webClientBuilder);
    }
}
