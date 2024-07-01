package com.example.CatchStudy.global.enums;

import lombok.Getter;

@Getter
public enum BookingStatus {

    // 입실 전, 입실 중, 이용 완료, 취소 됨
    beforeEnteringRoom,
    enteringRoom,
    completed,
    canceled
}
