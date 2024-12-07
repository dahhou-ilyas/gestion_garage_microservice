package org.example.gatewayservice.tokenVerification;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

public class TokenVerificationFilter implements GlobalFilter {

    private final WebClient webClient;
    private final ReactiveDiscoveryClient discoveryClient;

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
        String path = exchange.getRequest().getPath().toString();

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
                                    String publicKey = extractPublicKeyFromResponse(publicKeyResponse);
                                    verifyTokenWithPublicKey(token, publicKey);
                                    return chain.filter(exchange);
                                } catch (Exception e) {
                                    e.printStackTrace(); // Log détaillé pour le débogage
                                    return unauthorized(exchange);
                                }
                            });
                })
                .switchIfEmpty(unauthorized(exchange))
                .onErrorResume(ex -> {
                    ex.printStackTrace(); // Log d'erreur réseau
                    return unauthorized(exchange);
                });
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("WWW-Authenticate", "Bearer realm=\"Access to the API\"");
        return exchange.getResponse().setComplete();
    }

    private void verifyTokenWithPublicKey(String token, String publicKeyStr) throws Exception {
        String cleanKey = publicKeyStr
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        // Décoder la clé publique
        byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Valider le token
        Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token);
    }

    private String extractPublicKeyFromResponse(String publicKeyResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(publicKeyResponse).get("publicKey").asText();
    }
}
