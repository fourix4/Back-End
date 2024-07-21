package com.example.CatchStudy.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 리소스 허용할 URL 지정
        corsConfig.addAllowedOriginPattern("http://localhost:3000");
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")); // 허용 메소드 지정
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "content-type", "X-CSRF-Token")); // 허용 header 지정
        corsConfig.setAllowCredentials(true); // 인증, 인가 정보 허용

        // 웹소켓 연결 허용
        corsConfig.addAllowedOriginPattern("http://localhost:8080");
        corsConfig.addAllowedHeader("Sec-WebSocket-Extensions");
        corsConfig.addAllowedHeader("Sec-WebSocket-Key");
        corsConfig.addAllowedHeader("Sec-WebSocket-Protocol");
        corsConfig.addAllowedHeader("Sec-WebSocket-Version");

        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);
    }
}
