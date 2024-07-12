package com.example.CatchStudy.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeatType {

    seat("좌석"),
    room("스터디룸");

    private String message;
}
