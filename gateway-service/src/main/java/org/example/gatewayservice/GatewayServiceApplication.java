package org.example.gatewayservice;

import org.example.gatewayservice.tokenVerification.TokenVerificationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

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
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            exchange.getResponse().getHeaders().forEach((key, value) -> {
                System.out.println(key + ": " + value);
            });
        }));
    }

    @Bean
    public GlobalFilter combinedFilter(
            @Qualifier("customWebClientBuilder") WebClient.Builder webClientBuilder,
            ReactiveDiscoveryClient discoveryClient
    ) {
        return (exchange, chain) -> {
            // Token verification logging
            System.out.println("Verifying token for request");
            // Debug logging for request details
            System.out.println("Request Path: " + exchange.getRequest().getPath());
            System.out.println("Request Method: " + exchange.getRequest().getMethod());
            System.out.println("Request Headers: " + exchange.getRequest().getHeaders());

            // Create TokenVerificationFilter if needed

            TokenVerificationFilter tokenFilter = new TokenVerificationFilter(webClientBuilder, discoveryClient); //erruer dans le token filter on doit le faire comme le chaine .filter

            // Capture the original response body
            ServerHttpResponse originalResponse = exchange.getResponse();
            CachedBodyOutputMessage cachedBodyOutputMessage = new CachedBodyOutputMessage(exchange, originalResponse.getHeaders());

            return chain.filter(exchange.mutate().response(new CachedBodyServerHttpResponse(originalResponse, cachedBodyOutputMessage)).build())
                    .doOnError(error -> {
                        System.out.println(exchange.getResponse());
                        // Error logging
                        System.err.println("Error in filter chain: " + error);
                    })
                    .then(Mono.fromRunnable(() -> {
                        // Read and log the response body
                        DataBufferUtils.join(cachedBodyOutputMessage.getBody())
                                .subscribe(dataBuffer -> {
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    String bodyContent = new String(content, StandardCharsets.UTF_8);
                                    System.out.println("Response Body: " + bodyContent);

                                    // Important: release the buffer to prevent memory leaks
                                    DataBufferUtils.release(dataBuffer);
                                });
                    }));
        };

    }
}
