package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.dto.RoomDto;
import com.example.CatchStudy.domain.dto.SeatDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SeatingChartResponseDto {

    private String seating_chart; //좌석 배치도 이미지 주소
    private List<SeatDto> seats;
    private List<RoomDto> rooms;
    public static SeatingChartResponseDto toResponseDto(String seatingChart,List<SeatDto> seats,List<RoomDto> rooms){
        return new SeatingChartResponseDto(seatingChart,seats,rooms);
    }
}
