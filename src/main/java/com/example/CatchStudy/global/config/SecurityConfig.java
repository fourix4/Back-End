package com.example.CatchStudy.global.config;

import com.example.CatchStudy.global.jwt.JwtAuthenticationFilter;
import com.example.CatchStudy.global.oauth.AuthenticationSuccessHandler;
import com.example.CatchStudy.global.oauth.CustumOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig{

    private final CorsFilter corsFilter;
    private final CustumOauth2UserService custumOauth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .addFilter(corsFilter)
            .csrf(CsrfConfigurer::disable)
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(HttpBasicConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/manager/**").hasAnyRole("MANAGER")
                .anyRequest().permitAll()
            ).oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(custumOauth2UserService))
                .successHandler(authenticationSuccessHandler)
                .defaultSuccessUrl("http://localhost:3000/oauthkakao")
            ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
