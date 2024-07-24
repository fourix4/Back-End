package com.example.CatchStudy.domain.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
