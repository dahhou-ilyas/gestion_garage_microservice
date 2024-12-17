package org.example.gatewayservice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Utilisez setAllowedOriginPatterns pour une flexibilité maximale
        corsConfig.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://localhost:80",
                "http://127.0.0.1",
                "http://localhost",
                "http://localhost:3000",  // Ajoutez d'autres ports si nécessaire
                "http://127.0.0.1:5173"   // Incluez également les adresses IP locales
        ));

        corsConfig.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
        ));

        // Précisez les en-têtes autorisés de manière plus explicite
        corsConfig.setAllowedHeaders(List.of("*"));

        // Exposez les en-têtes personnalisés si nécessaire
        corsConfig.setExposedHeaders(List.of("Authorization", "Content-Type"));

        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}