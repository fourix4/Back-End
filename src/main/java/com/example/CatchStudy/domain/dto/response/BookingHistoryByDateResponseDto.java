package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.dto.BookingHistoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BookingHistoryByDateResponseDto {

    private List<BookingHistoryDto> booking_list;
    public static BookingHistoryByDateResponseDto toResponseDto(List<BookingHistoryDto> bookingList){
        return new BookingHistoryByDateResponseDto(bookingList);
    }
}
