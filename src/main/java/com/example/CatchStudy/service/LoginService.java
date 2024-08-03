package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.request.OauthCodeRequestDto;
import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.global.jwt.JwtToken;
import com.example.CatchStudy.global.jwt.JwtUtil;
import com.example.CatchStudy.global.oauth.GoogleOAuthToken;
import com.example.CatchStudy.global.oauth.GoogleProfile;
import com.example.CatchStudy.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    @Value("${google.client_id}")
    private String clientId;
    @Value("${google.client_secret}")
    private String clientSecret;
    @Value("${google.grant_type}")
    private String grantType;
    @Value("${google.redirect_uri}")
    private String redirectUri;
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;

    @Transactional
    public String googleLogin(OauthCodeRequestDto oauthCodeRequestDto) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders tokenHeaders = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOAuthToken oAuthToken = null;
        GoogleProfile googleProfile = null;

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", oauthCodeRequestDto.getCode());
        params.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, tokenHeaders);

        // google에서 token 받기
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );
        log.info("[getGoogleProfile] code로 인증을 받은뒤 응답받은 token 값 : {}",response);
        // token에서 값 추출
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);
        } catch (Exception e) {
            log.info("token error : " + e.getMessage());
            throw new CatchStudyException(ErrorCode.INVALID_OAUTH_TOKEN_ERROR);
        }

        HttpHeaders profileHeaders = new HttpHeaders();
        // 헤더에 토큰값 설정
        profileHeaders.add("Authorization", "Bearer "+oAuthToken.getAccess_token());
        HttpEntity<MultiValueMap<String,String>>googleProfileRequest = new HttpEntity<>(profileHeaders);
        // token을 사용하여 사용자 정보 받기
        ResponseEntity<String>googleProfileResponse = rt.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                googleProfileRequest,
                String.class
        );
        log.info("[google] 구글 프로필 response :{}",googleProfileResponse);
        try {
            googleProfile = objectMapper.readValue(googleProfileResponse.getBody(), GoogleProfile.class);
        } catch (Exception e) {
            log.info("profile error : " + e.getMessage());
            throw new CatchStudyException(ErrorCode.INVALID_OAUTH_TOKEN_ERROR);
        }

        Users user = usersRepository.findByEmail(googleProfile.getEmail());
        if(user == null) user = usersRepository.save(googleProfile.toEntity());
        JwtToken jwtToken = jwtUtil.generatedToken(user.getEmail(), user.getAuthor());

        return jwtToken.getAccessToken();
    }
}
