package com.example.CatchStudy.domain.dto;

import com.example.CatchStudy.global.enums.SeatType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingHistoryDto {

    private Long booking_id;
    private Long cafe_id;
    private String cafe_name;
    private String cafe_thumbnail_url;
    private SeatType type;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime payment_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime start_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime end_time;
    private Integer amount;
    private String status;

    public static BookingHistoryDto toDto(Long bookingId,Long cafeId,String cafeName,String cafeThumbnailUrl,SeatType type,String address,LocalDateTime paymentTime,LocalDateTime startTime,LocalDateTime endTime,Integer amount,String status){
        return new BookingHistoryDto(bookingId,cafeId,cafeName,cafeThumbnailUrl,type,address,paymentTime,startTime,endTime,amount,status);
    }
}
