package com.example.CatchStudy.domain.dto;

import com.example.CatchStudy.global.enums.PaymentType;
import com.example.CatchStudy.global.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SeatBookingDto {

    Long userId;
    Long cafeId;
    Long seatId;
    Integer time;
    Integer amount;
    PaymentType paymentType;
    Long roomId;
    SeatType type;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public static SeatBookingDto of(Long userId, Long cafeId, Long seatId, Integer time, Integer amount, PaymentType paymentType, SeatType type) {
        return new SeatBookingDto(userId, cafeId, seatId, time, amount, paymentType, null, type, null, null);
    }

    public static SeatBookingDto of(Long userId, Long cafeId, Integer amount, PaymentType paymentType, Long roomId, SeatType type, LocalDateTime startTime, LocalDateTime endTime) {
        return new SeatBookingDto(userId, cafeId, null, null, amount, paymentType, roomId, type, startTime, endTime);
    }
}
