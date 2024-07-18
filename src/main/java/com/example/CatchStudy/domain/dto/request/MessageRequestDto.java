package com.example.CatchStudy.domain.dto.request;

import lombok.Getter;

@Getter
public class MessageRequestDto {

    private long userId;
    private long chatRoomId;
    private String chat;
}
