package org.example.gatewayservice.tokenVerification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TokenVerificationFilter implements GlobalFilter {

    private final WebClient.Builder webClientBuilder;


    public TokenVerificationFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authorizationHeader.substring(7);

        return webClientBuilder.build()
                .get()
                .uri("http://auth-service/api/auth/public-key")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(publicKeyResponse -> {
                    // Assurez-vous d'utiliser un service pour vérifier le token avec la clé publique
                    try {
                        // Utilisez la clé publique pour vérifier le token ici
                        // Cette vérification peut être effectuée par un service externe ou une méthode dans le même filtre
                        verifyTokenWithPublicKey(token, publicKeyResponse);
                        return chain.filter(exchange);
                    } catch (Exception e) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                });
    }

    private void verifyTokenWithPublicKey(String token, String publicKey) {

    }
}
