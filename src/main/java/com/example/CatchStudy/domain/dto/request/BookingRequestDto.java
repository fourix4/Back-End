package com.example.CatchStudy.domain.dto.request;

import com.example.CatchStudy.domain.dto.SeatBookingDto;
import com.example.CatchStudy.global.enums.PaymentType;
import com.example.CatchStudy.global.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {
    Long cafe_id;
    Long seat_id;
    Integer time;
    Integer amount;

    PaymentType payment_type;

    Long room_id;
    SeatType type;
    String start_time;

    public static BookingRequestDto of(Long cafe_id, Long seat_id, Integer time, Integer amount, PaymentType payment_type, SeatType type) {
        return new BookingRequestDto(cafe_id, seat_id, time, amount, payment_type, null, type, null);
    }
    public static BookingRequestDto of(Long cafe_id, Integer time, Integer amount, PaymentType payment_type, Long room_Id, SeatType type, String start_time) {
        return new BookingRequestDto(cafe_id, null,time,amount,payment_type,room_Id,type,start_time);
    }

    public SeatBookingDto toSeatDto() {
        return SeatBookingDto.of(
                cafe_id,
                seat_id,
                time,
                amount,
                payment_type,
                type
        );
    }

    public SeatBookingDto toRoomDto() {
        return SeatBookingDto.of(
                cafe_id,
                time,
                amount,
                payment_type,
                room_id,
                type,
                start_time

        );
    }
}
