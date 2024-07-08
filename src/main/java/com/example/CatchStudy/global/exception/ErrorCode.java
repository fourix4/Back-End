package com.example.CatchStudy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    SEAT_NOT_AVAILABLE(HttpStatus.NOT_ACCEPTABLE, "이미 예약된 좌석입니다"),
    ROOM_NOT_AVILABLE(HttpStatus.NOT_ACCEPTABLE,"이미 예약된 룸입니다"),
    STUDYCAFE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스터디카페를 찾을 수 없습니다"),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 좌석을 찾을 수 없습니다"),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스터디룸을 찾을 수 없습니다"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 결제를 찾을 수 없습니다"),
    PAYMENT_CANCELED(HttpStatus.NOT_ACCEPTABLE,"결제가 취소되었습니다"),
    PAYMENT_FAIL(HttpStatus.NOT_ACCEPTABLE,"결제가 실패되었습니다"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"데이터베이스 오류입니다"),

    ;

    private HttpStatus status;
    private String message;
}
