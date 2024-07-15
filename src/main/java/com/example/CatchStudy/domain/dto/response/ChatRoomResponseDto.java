package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private long chatRoomId;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getChatRoomId();
    }
}
