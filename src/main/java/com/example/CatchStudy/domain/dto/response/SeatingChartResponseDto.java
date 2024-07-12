package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.dto.RoomDto;
import com.example.CatchStudy.domain.dto.SeatDto;
import com.example.CatchStudy.domain.dto.UsageFeeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SeatingChartResponseDto {

    private String seating_chart; //좌석 배치도 이미지 주소
    private List<SeatDto> seats;
    private List<RoomDto> rooms;
    private List<UsageFeeDto> usage_fee;

    public static SeatingChartResponseDto toResponseDto(String seatingChart, List<SeatDto> seats, List<RoomDto> rooms, List<UsageFeeDto> usageFee) {
        return new SeatingChartResponseDto(seatingChart, seats, rooms, usageFee);
    }
}
