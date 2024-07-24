package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.Room;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomInfoResponseDto {
    private Long cancelAvailableTime;
    private List<RoomResponseDto> rooms;

    public RoomInfoResponseDto(long cancelAvailableTime, List<RoomResponseDto> rooms) {
        this.cancelAvailableTime = cancelAvailableTime;
        this.rooms = rooms;
    }
}
