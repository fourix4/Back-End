package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.ChatRoomRequestDto;
import com.example.CatchStudy.domain.dto.response.ChatRoomResponseDto;
import com.example.CatchStudy.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @PostMapping("")
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {

        return chatService.createChatRoom(chatRoomRequestDto);
    }
}
