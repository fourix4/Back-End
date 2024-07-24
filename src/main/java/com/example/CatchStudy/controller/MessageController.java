package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.MessageRequestDto;
import com.example.CatchStudy.domain.dto.response.MessageResponseDto;
import com.example.CatchStudy.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;

    @MessageMapping("/{chatRoomId}/chat")
    @SendTo("/sub/{chatRoomId}/chat")
    public void createMessage(@DestinationVariable long chatRoomId, MessageRequestDto messageRequestDto) {
        MessageResponseDto messageResponseDto = chatService.createMessage(chatRoomId, messageRequestDto);
    }
}
