package com.example.CatchStudy.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RoomInfoRequestDto {

    private Long cancelAvailableTime;
    private List<RoomsRequestDto> rooms;
}
