package org.example.gatewayservice.tokenVerification;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;

public class TokenVerificationFilter implements GlobalFilter {

    private final WebClient webClient;
    private final ReactiveDiscoveryClient discoveryClient;

    // Liste des endpoints à ignorer
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/public-key"
    );

    public TokenVerificationFilter(WebClient.Builder webClientBuilder, ReactiveDiscoveryClient discoveryClient) {
        this.webClient = webClientBuilder.build();
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Récupérer le chemin de la requête
        String path = exchange.getRequest().getPath().toString();

        // Ignorer la vérification pour les chemins exclus
        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authorizationHeader.substring(7);

        return discoveryClient.getInstances("AUTH-SERVICE")
                .next()
                .flatMap(serviceInstance -> {
                    String baseUrl = serviceInstance.getUri().toString();
                    return webClient.get()
                            .uri(baseUrl + "/api/auth/public-key")
                            .retrieve()
                            .bodyToMono(String.class)
                            .flatMap(publicKeyResponse -> {
                                try {
                                    System.out.println("xxxxxxxxxxxxx");
                                    System.out.println(publicKeyResponse);
                                    System.out.println("xxxxxxxxxxxxxx");
                                    verifyTokenWithPublicKey(token, publicKeyResponse);
                                    return chain.filter(exchange);
                                } catch (Exception e) {
                                    return unauthorized(exchange);
                                }
                            });
                })
                .switchIfEmpty(unauthorized(exchange))
                .onErrorResume(ex -> unauthorized(exchange));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private void verifyTokenWithPublicKey(String token, String publicKeyStr) {
        try {
            // Decode the public key
            byte[] keyBytes = Decoders.BASE64.decode(publicKeyStr);
            Key key = Keys.hmacShaKeyFor(keyBytes);

            // Validate the token
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}