package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RoomInfoResponseDto {
    private Long cancelAvailableTime;
    private List<RoomResponseDto> rooms;

    public RoomInfoResponseDto(long cancelAvailableTime, List<RoomResponseDto> rooms) {
        this.cancelAvailableTime = cancelAvailableTime;
        this.rooms = rooms;
    }
}
