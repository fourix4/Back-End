package com.example.CatchStudy.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;

@Configuration
public class CorsConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 리소스 허용할 URL 지정
        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("http://localhost:8080");
        allowedOriginPatterns.add("http://localhost:3000");
        allowedOriginPatterns.add(redisHost+":6739");
        corsConfig.setAllowedOrigins(allowedOriginPatterns);

        corsConfig.addAllowedMethod("*");        // 허용 메소드 지정
        corsConfig.addAllowedHeader("*");        // 허용 header 지정
        corsConfig.setAllowCredentials(true);    // 인증, 인가 정보 허용

        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);
    }
}
