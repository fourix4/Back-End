package com.example.CatchStudy.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatRequestDto {

    private String seatNumber;
    private boolean isAvailable;
}
