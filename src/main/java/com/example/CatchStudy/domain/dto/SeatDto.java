package com.example.CatchStudy.domain.dto;

import com.example.CatchStudy.domain.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatDto {

    private Long seat_id;
    private String seat_number;
    private Boolean isAvailable;

    public static SeatDto from(Seat entity) {
        return new SeatDto(
                entity.getSeatId(),
                entity.getSeatNumber(),
                entity.getIsAvailable()
        );
    }
}
