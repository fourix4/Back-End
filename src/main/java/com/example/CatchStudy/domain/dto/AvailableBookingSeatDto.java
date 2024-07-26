package com.example.CatchStudy.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AvailableBookingSeatDto {

    private Long booking_id;
    private Long cafe_id;
    private String cafe_name;
    private String status;
    private Integer amount;
    private String address;
    private String seat_number;
    private String code;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime payment_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime start_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime end_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime start_available_time;

    public static AvailableBookingSeatDto toDto(Long bookingId,Long cafeId,String cafeName,String status,
                                                Integer amount,String address,String seatNumber,String code,LocalDateTime paymentTime,
                                                LocalDateTime startTime,LocalDateTime endTime,LocalDateTime startAvailableTime) {
        return new AvailableBookingSeatDto(
                bookingId,
                cafeId,
                cafeName,
                status,
                amount,
                address,
                seatNumber,
                code,
                paymentTime,
                startTime,
                endTime,
                startAvailableTime
        );
    }
}
