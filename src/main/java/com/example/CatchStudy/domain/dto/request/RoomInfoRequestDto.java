package com.example.CatchStudy.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfoRequestDto {

    private Long cancelAvailableTime;
    private List<RoomsRequestDto> rooms;
}
