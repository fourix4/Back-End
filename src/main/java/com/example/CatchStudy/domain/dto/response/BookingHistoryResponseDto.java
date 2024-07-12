package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.dto.BookingHistoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BookingHistoryResponseDto {

    private List<BookingHistoryDto> booking_list;
    public static BookingHistoryResponseDto toResponseDto(List<BookingHistoryDto> bookingList){
        return new BookingHistoryResponseDto(bookingList);
    }
}
