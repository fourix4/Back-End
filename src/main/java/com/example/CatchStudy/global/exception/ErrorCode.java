package com.example.CatchStudy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    SEAT_NOT_AVAILABLE(HttpStatus.NOT_ACCEPTABLE, "seat not available"),
    STUDYCAFE_NOT_FOUND(HttpStatus.NOT_FOUND, "studyCafe not found"),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "seat not found"),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "room not found"),
    PAYMENT_NOT_POSSIBLE(HttpStatus.NOT_ACCEPTABLE, "payment not possible");

    private HttpStatus status;
    private String message;
}
