package com.example.CatchStudy.domain.dto;

import com.example.CatchStudy.global.enums.PaymentType;
import com.example.CatchStudy.global.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeatBookingDto {

    Long cafeId;
    Long seatId;
    Integer time;
    Integer amount;
    PaymentType paymentType;
    Long roomId;
    SeatType type;
    String startTime;

    public static SeatBookingDto of(Long cafeId, Long seatId, Integer time, Integer amount, PaymentType paymentType, SeatType type) {
        return new SeatBookingDto(cafeId, seatId, time, amount, paymentType, null, type, null);
    }

    public static SeatBookingDto of(Long cafeId, Integer time, Integer amount, PaymentType paymentType, Long roomId, SeatType type, String startTime) {
        return new SeatBookingDto(cafeId, null, time, amount, paymentType, roomId, type, startTime);
    }
}
