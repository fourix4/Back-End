package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.Message;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {

    private final long userId;
    private final long messageId;
    private final String chat;
    private final LocalDateTime createDate;

    public MessageResponseDto(Message message) {
        this.userId = message.getUser().getUserId();
        this.messageId = message.getMessageId();
        this.chat = message.getChat();
        this.createDate = message.getCreateDate();
    }
}
