package com.example.CatchStudy.domain.dto.request;

import com.example.CatchStudy.domain.dto.SeatBookingDto;
import com.example.CatchStudy.global.enums.PaymentType;
import com.example.CatchStudy.global.enums.SeatType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class BookingRequestDto {
    @JsonProperty("cafe_id")
    Long cafe_id;

    @JsonProperty("seat_id")
    Long seat_id;
    @JsonProperty("time")
    Integer time;
    @JsonProperty("amount")
    Integer amount;
    @JsonProperty("payment_type")
    String payment_type;
    @JsonProperty("room_id")
    Long room_id;
    @JsonProperty("type")
    String type;
    @JsonProperty("start_time")
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
