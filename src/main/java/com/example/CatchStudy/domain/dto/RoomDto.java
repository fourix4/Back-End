package com.example.CatchStudy.domain.dto;

import com.example.CatchStudy.domain.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomDto {

    private Long room_id;
    private String room_name;
    private Integer capacity;
    private Long cancel_available_time;

    public static RoomDto from(Room entity) {
        return new RoomDto(
                entity.getRoomId(),
                entity.getRoomName(),
                entity.getCapacity(),
                entity.getCancelAvailableTime()
        );
    }
}
