package com.example.CatchStudy.global.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "catch-study:";

    public void save(String refreshToken, String accessToken) {
        redisTemplate.opsForValue().set(KEY_PREFIX + accessToken, refreshToken, 7, TimeUnit.DAYS);   // 7일간 저장 후 삭제
    }

    public String find(String accessToken) {
        log.info(accessToken);
        return redisTemplate.opsForValue().get(KEY_PREFIX + accessToken);
    }

    public void delete(String accessToken) {
        redisTemplate.delete(KEY_PREFIX + accessToken);
    }
}
