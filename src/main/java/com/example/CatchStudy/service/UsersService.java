package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.response.AccessTokenResponseDto;
import com.example.CatchStudy.domain.dto.response.UsersResponseDto;
import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.enums.Author;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.global.jwt.JwtToken;
import com.example.CatchStudy.global.jwt.JwtUtil;
import com.example.CatchStudy.global.jwt.RefreshTokenRepository;
import com.example.CatchStudy.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class  UsersService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void logout(String token) {
        String accessToken = token.substring(7);
        refreshTokenRepository.delete(accessToken);
        SecurityContextHolder.clearContext(); // 로그아웃 후 보안 컨텍스트 정리
    }

    public AccessTokenResponseDto reissuanceAccessToken(String token) {
        String accessToken = token.substring(7);
        String refreshToken = refreshTokenRepository.find(accessToken);
        if(refreshToken == null) throw new CatchStudyException(ErrorCode.EXPIRED_LOGIN_ERROR);

        refreshTokenRepository.delete(accessToken);
        String email = jwtUtil.getEmailFromRefreshToken(refreshToken);
        Users user = usersRepository.findByEmail(email);

        JwtToken jwtToken = jwtUtil.generatedToken(user.getEmail(), user.getAuthor());

        return new AccessTokenResponseDto(jwtToken.getAccessToken());
    }

    public long getCurrentUserId() {
        String email = getEmail();
        Users users = usersRepository.findByEmail(email);

        return users.getUserId();
    }

    public void deleteUser() {
        String email = getEmail();
        usersRepository.deleteByEmail(email);
    }

    public UsersResponseDto getUserInfo() {
        String email = getEmail();

        return new UsersResponseDto(usersRepository.findByEmail(email));
    }

    private String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) email = userDetails.getUsername();
        else throw new CatchStudyException(ErrorCode.USER_NOT_FOUND);

        return email;
    }
}
