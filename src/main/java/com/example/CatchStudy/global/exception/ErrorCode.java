package com.example.CatchStudy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    EXPIRED_LOGIN_ERROR(HttpStatus.FORBIDDEN, "만료된 로그인입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "권한이 없는 유저입니다."),
    SEAT_NOT_AVAILABLE(HttpStatus.NOT_ACCEPTABLE, "이미 예약된 좌석입니다"),
    ROOM_NOT_AVAILABLE(HttpStatus.NOT_ACCEPTABLE, "이미 예약된 룸입니다"),
    STUDYCAFE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스터디카페를 찾을 수 없습니다"),

    STUDYCAFE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"스터디카페 이미 사진을 찾을 수 없습니다"),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 좌석을 찾을 수 없습니다"),
    SEAT_NOT_SELECT(HttpStatus.NOT_ACCEPTABLE, "해당 좌석을 선택할 수 없습니다"),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스터디룸을 찾을 수 없습니다"),

    ROOM_NOT_SELECT(HttpStatus.NOT_ACCEPTABLE, "해당 스터디룸을 선택할 수 없습니다"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 결제를 찾을 수 없습니다"),
    PAYMENT_CANCELED(HttpStatus.NOT_ACCEPTABLE, "결제가 취소되었습니다"),
    PAYMENT_FAIL(HttpStatus.NOT_ACCEPTABLE, "결제가 실패되었습니다"),

    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류입니다"),
    CANCEL_POSSIBLE_TIME_PASSED(HttpStatus.NOT_ACCEPTABLE, "취소 가능한 시간이 지났습니다"),
    BOOKING_SEAT_NOT_FOUND(HttpStatus.NOT_FOUND,"예약한 좌석이 존재하지 않습니다"),

    ENTERING_SEAT_NOT_FOUND(HttpStatus.NOT_FOUND,"입실 중인 좌석이 존재하지 않습니다"),

    //S3 Error
    EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "파일이 없습니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 에러가 발생했습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "잘못된 확장자입니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제 중 에러가 발생했습니다."),

    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."),

    //Quartz Error
    QUARTZ_SCHEDULER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"스케줄러 오류입니다"),

    ;

    private HttpStatus status;
    private String message;
}
