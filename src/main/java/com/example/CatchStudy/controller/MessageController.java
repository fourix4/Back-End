package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.MessageRequestDto;
import com.example.CatchStudy.domain.dto.response.MessageResponseDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/{chatRoomId}/chat")
    public void createMessage(@DestinationVariable long chatRoomId, MessageRequestDto messageRequestDto) {
        MessageResponseDto messageResponseDto = messageService.createMessage(chatRoomId, messageRequestDto);
        simpMessagingTemplate.convertAndSend("/sub/" + messageRequestDto.getChatRoomId() + "/chat");
    }
}
