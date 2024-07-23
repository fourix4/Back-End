package com.example.CatchStudy.global.config;

import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.global.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = "";

        System.out.println("-------------accessor : " + accessor);
        System.out.println("--------accessor header : " + accessor.getNativeHeader("Authorization"));
        // 연결, 해제, 메시지 전송 요청에 대해 실행
        if((accessor.getCommand() == StompCommand.CONNECT) || (accessor.getCommand() == StompCommand.SEND)
                || (accessor.getCommand() == StompCommand.SUBSCRIBE)) {

            accessToken = accessor.getFirstNativeHeader("Authorization");

            if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
                accessToken = accessToken.substring(7);
            }

            try {
                jwtUtil.validateAccessToken(accessToken);
            } catch (ExpiredJwtException e) {
                throw new CatchStudyException(ErrorCode.EXPIRED_ACCESS_TOKEN);
            }

            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            accessor.setUser(authentication);
        }

        return message;
    }
}
