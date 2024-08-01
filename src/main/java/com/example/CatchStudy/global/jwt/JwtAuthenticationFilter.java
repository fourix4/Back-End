package com.example.CatchStudy.global.jwt;

import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveToken(request);
        String uri = request.getRequestURI();

        List<String> excludedPaths = Arrays.asList(
                "/api/users/reissuance",
                "/api/user/login/google",
                "/api/studycafes/search"
        );

        List<Pattern> excludedPatterns = Arrays.asList(
                Pattern.compile("^/api/studycafes/\\d+$"), // /api/studycafes/{cafe_id}
                Pattern.compile("^/api/studycafes/\\d+/seatingchart$") // /api/studycafes/{cafe_id}/seatingchart
        );

        if (excludedPaths.contains(uri) || excludedPatterns.stream().anyMatch(pattern -> pattern.matcher(uri).matches())) {
            filterChain.doFilter(request, response);
            return;
        }

        if(accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // accessToken 검증
        try {
            jwtUtil.validateAccessToken(accessToken);
            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication); // 검증 후 security context 인증 정보 저장
        } catch (ExpiredJwtException e) {   // 만료 에러 발생
            // refreshToken 존재시
            if (jwtUtil.validateRefreshToken(accessToken)) {
                sendErrorResponse(response, "401", ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage());
            } else {
                sendErrorResponse(response, "403", ErrorCode.EXPIRED_LOGIN_ERROR.getMessage());
            }

            return;
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

     private static void sendErrorResponse(HttpServletResponse response, String statusCode, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Response<Void> errorResponse = Response.error(statusCode, message);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

}
