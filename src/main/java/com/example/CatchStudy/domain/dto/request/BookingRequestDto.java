package com.example.CatchStudy.domain.dto.request;

import com.example.CatchStudy.domain.dto.SeatBookingDto;
import com.example.CatchStudy.global.enums.PaymentType;
import com.example.CatchStudy.global.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingRequestDto {
    Long cafe_id;
    Long seat_id;
    Integer time;
    Integer amount;

    PaymentType payment_type;

    Long room_id;
    SeatType type;
    LocalDateTime start_time;
    LocalDateTime end_time;

    public static BookingRequestDto of(Long cafe_id, Long seat_id, Integer time, Integer amount, PaymentType payment_type, SeatType type) {
        return new BookingRequestDto(cafe_id, seat_id, time, amount, payment_type, null, type, null, null);
    }

    public static BookingRequestDto of(Long cafe_id, Integer amount, PaymentType payment_type, Long room_Id, SeatType type, LocalDateTime start_time, LocalDateTime end_time) {
        return new BookingRequestDto(cafe_id, null, null, amount, payment_type, room_Id, type, start_time, end_time);
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
                amount,
                payment_type,
                room_id,
                type,
                start_time,
                end_time

        );
    }
}
