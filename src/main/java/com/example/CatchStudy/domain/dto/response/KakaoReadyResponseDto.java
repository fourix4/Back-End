package com.example.CatchStudy.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoReadyResponseDto {

    private String tid; //결제 고유 번호
    private String next_redirect_pc_url;  // pc 웹일 경우 받는 결제 페이지
    private String created_at; //결제 준비 요청 시간


    public BookingResponseDto toDto() {
        return BookingResponseDto.of(
                next_redirect_pc_url
        );
    }
}
