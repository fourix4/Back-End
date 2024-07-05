package com.example.CatchStudy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"internal server error"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"user not found"),
    SEAT_NOT_AVAILABLE(HttpStatus.NOT_ACCEPTABLE,"seat not available"),
    STUDYCAFE_NOT_FOUND(HttpStatus.NOT_FOUND,"studyCafe not found"),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND,"seat not found"),
    PAYMENT_CANCELED(HttpStatus.NOT_ACCEPTABLE,"payment canceled")
    ;
    private HttpStatus status;
    private String message;
}
