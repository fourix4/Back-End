package com.example.CatchStudy.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AvailableRoomTimeResponseDto {

    private List available_start_time;

    public static AvailableRoomTimeResponseDto toResponseDto(List<String> times){
        return new AvailableRoomTimeResponseDto(times);
    }

}
