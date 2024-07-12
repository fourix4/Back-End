package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
