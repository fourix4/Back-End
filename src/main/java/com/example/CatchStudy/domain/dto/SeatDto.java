package com.example.CatchStudy.domain.dto;

import com.example.CatchStudy.domain.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatDto {

    private Long seatId;
    private StudyCafeDto studyCafeDto;
    private String seatNumber;
    private Boolean isAvailable;

    public static SeatDto from(Seat entity) {
        return new SeatDto(
                entity.getSeatId(),
                StudyCafeDto.from(entity.getStudyCafe()),
                entity.getSeatNumber(),
                entity.getIsAvailable()
        );
    }
}
