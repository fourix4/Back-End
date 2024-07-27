package com.example.CatchStudy.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ChatRoomRequestDto {
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("cafe_id")
    private long cafeId;
}
