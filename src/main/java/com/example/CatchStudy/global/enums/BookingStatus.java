package com.example.CatchStudy.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookingStatus {

    // 입실 전, 입실 중, 이용 완료, 취소 됨
    beforeEnteringRoom("입실 전"),
    enteringRoom("입실 중"),
    completed("이용 완료"),
    canceled("취소됨");

    private String message;
}
