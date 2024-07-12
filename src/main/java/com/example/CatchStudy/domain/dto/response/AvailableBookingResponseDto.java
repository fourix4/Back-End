package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.dto.AvailableBookingRoomDto;
import com.example.CatchStudy.domain.dto.AvailableBookingSeatDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AvailableBookingResponseDto {

    private List<AvailableBookingSeatDto> seat_list;
    private List<AvailableBookingRoomDto> room_list;

    public static AvailableBookingResponseDto toResponseDto(List<AvailableBookingSeatDto> seatList, List<AvailableBookingRoomDto> roomList) {
        return new AvailableBookingResponseDto(seatList, roomList);
    }
}
