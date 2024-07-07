package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.response.AccessTokenResponseDto;
import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.jwt.JwtToken;
import com.example.CatchStudy.global.jwt.JwtUtil;
import com.example.CatchStudy.global.jwt.RefreshTokenRepository;
import com.example.CatchStudy.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class  UsersService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void logout() {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenRepository.delete(users.getEmail());
        SecurityContextHolder.clearContext(); // 로그아웃 후 보안 컨텍스트 정리
    }

    public AccessTokenResponseDto reissuanceAccessToken() {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JwtToken jwtToken = jwtUtil.generatedToken(users.getEmail(), String.valueOf(users.getAuthor()));

        return new AccessTokenResponseDto(jwtToken.getAccessToken());
    }
}
