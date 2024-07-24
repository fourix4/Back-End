package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.ChatRoom;
import com.example.CatchStudy.domain.entity.Message;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRoomResponseDto {

    private long chatRoomId;
    private long cafeId;
    private String cafeName;
    private String lastChat;
    private LocalDateTime lastChatDate;
    private boolean status;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getChatRoomId();
    }

    public ChatRoomResponseDto(ChatRoom chatRoom, Message message, boolean status) {
        this.chatRoomId = chatRoom.getChatRoomId();
        this.cafeId = chatRoom.getStudyCafe().getCafeId();
        this.cafeName = chatRoom.getStudyCafe().getCafeName();
        this.lastChat = message.getChat();
        this.lastChatDate = message.getCreateDate();
        this.status = status;
    }
}
