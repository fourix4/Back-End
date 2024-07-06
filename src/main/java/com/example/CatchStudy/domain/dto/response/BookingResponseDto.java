package com.example.CatchStudy.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingResponseDto {

    private String next_redirect_pc_url;

    public static BookingResponseDto of(String next_redirect_pc_url) {
        return new BookingResponseDto(next_redirect_pc_url);
    }
}
