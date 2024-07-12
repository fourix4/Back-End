package com.example.CatchStudy.domain.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StudyCafeSearchResponseDto {

    private Long cafeId;
    private String cafeName;
    private String address;
    private String thumbnail;

    public StudyCafeSearchResponseDto(long cafeId, String cafeName, String address, String thumbnail) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.address = address;
        this.thumbnail = thumbnail;
    }
}
