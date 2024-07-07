package com.example.CatchStudy.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;


@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "catch-study:";

    public void save(String refreshToken, String email) {
        redisTemplate.opsForValue().set(KEY_PREFIX + email, refreshToken, 7, TimeUnit.DAYS);   // 7일간 저장 후 삭제
    }

    public String find(String email) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + email);
    }

    public void delete(String email) {
        redisTemplate.delete(KEY_PREFIX + email);
    }
}
