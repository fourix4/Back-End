package com.example.CatchStudy.global.oauth;

import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.jwt.JwtToken;
import com.example.CatchStudy.global.jwt.JwtUtil;
import com.example.CatchStudy.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        Users users = usersRepository.findByEmail(email);
        JwtToken jwtToken = jwtUtil.generatedToken(email, users.getAuthor());

        // 요청의 호스트 정보 가져오기
        String host = request.getHeader("Host");
        // 프로토콜 정보 가져오기 (http 또는 https)
        String scheme = request.getScheme();
        String redirectUrl = scheme + "://" + host + "/oauthkakao?accessToken=" + jwtToken.getAccessToken();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}