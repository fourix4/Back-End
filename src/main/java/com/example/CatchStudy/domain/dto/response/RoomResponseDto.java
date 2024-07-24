package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.Room;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomResponseDto {

    private String roomName;
    private int capacity;
    private int price;

    public RoomResponseDto(Room room) {
        this.roomName = room.getRoomName();
        this.capacity = room.getCapacity();
        this.price = room.getPrice();
    }
}
