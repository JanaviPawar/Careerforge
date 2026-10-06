// src/main/java/com/careerforge/config/CorsConfig.java
// Add this file to your project — without it the frontend CANNOT talk to backend
package com.careerforge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class corsconfig {
    /*
     * CORS = Cross-Origin Resource Sharing.
     * When your frontend (github.io) calls your backend (railway.app),
     * the browser blocks it unless the backend explicitly says "this origin is allowed".
     * Without this file: browser shows "CORS error", frontend shows nothing.
     * With this file: frontend can call every /api/** endpoint freely.
     */

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();


        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5500",
                "http://127.0.0.1:5500",
                "https://github.com/JanaviPawar"
        ));

        // Allow all common HTTP methods
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));

        // Allow all headers (including Authorization for JWT)
        config.setAllowedHeaders(List.of("*"));

        // Allow sending cookies/auth headers
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
