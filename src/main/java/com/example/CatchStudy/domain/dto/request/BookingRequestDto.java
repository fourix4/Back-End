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

    String payment_type;

    Long room_id;
    String type;
    String start_time;

    public SeatBookingDto toSeatDto() {
        return SeatBookingDto.of(
                cafe_id,
                seat_id,
                time,
                amount,
                PaymentType.valueOf(payment_type),
                SeatType.valueOf(type)
        );
    }

    public SeatBookingDto toRoomDto() {
        return SeatBookingDto.of(
                cafe_id,
                time,
                amount,
                PaymentType.valueOf(payment_type),
                room_id,
                SeatType.valueOf(type),
                start_time

        );
    }
}
