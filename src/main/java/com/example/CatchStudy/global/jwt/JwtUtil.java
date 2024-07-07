package com.example.CatchStudy.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final String AUTHORITIES_KEY = "author";
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtUtil(@Value("${jwt.secret.key}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 발급
    public JwtToken generatedToken(String email, String author) {

        String accessToken = createAccessToken(email, author);
        String refreshToken = createRefreshToken(email, author);

        refreshTokenRepository.save(refreshToken, email);   // refresh token redis 저장

        return new JwtToken(accessToken, refreshToken);
    }


    // Access Token 생성
    public String createAccessToken(String email, String author) {

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, author)
                .setIssuedAt(new Date())    // 발행 시간
                .setExpiration(new Date((new Date()).getTime() + 1800000))  // 유효기간 30분
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(String email, String author) {

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, author)
                .setIssuedAt(new Date())    // 발행 시간
                .setExpiration(new Date((new Date()).getTime() + 604800000))  // 유효기간 1주
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // token 검증
    public void validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
            .setSigningKey(key)
            .build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("지원하지 않은 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT 클레임 문자열이 비어 있습니다.");
        }
    }

    public boolean validateRefreshToken(String email) {
        return refreshTokenRepository.find(email) != null;
    }

    // token으로 Authentication 객체 만들기
    public Authentication getAuthentication(String accessToken) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        User user = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(user, accessToken, authorities);
    }
}
