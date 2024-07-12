package com.example.CatchStudy.global.oauth;

import com.example.CatchStudy.global.jwt.JwtToken;
import com.example.CatchStudy.global.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 인증된 사용자 정보를 oauth 로 캐스팅
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String author = oAuth2User.getAuthorities().stream().findFirst().orElseThrow().getAuthority();    // author 가져오기

        JwtToken jwtToken = jwtUtil.generatedToken(email, author);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("code", 200);
        responseData.put("message", "success");

        Map<String, Map<String, String>> data = new HashMap<>();
        Map<String, String> result = new HashMap<>();
        result.put("accessToken", jwtToken.getAccessToken());
        data.put("result", result);
        responseData.put("data", data);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
        response.sendRedirect("http://localhost:3000/oauthkakao");
    }
}