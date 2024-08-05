package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.StudyCafe;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ManagerListResponseDto {

    private long cafeId;
    private String cafeName;
    private AddressResponseDto address;
    private Integer usingSeats;
    private Integer seats;
    private Integer sales;

    public ManagerListResponseDto(StudyCafe studyCafe, int usingSeats, int seats, int sales) {
        this.cafeId = studyCafe.getCafeId();
        this.cafeName = studyCafe.getCafeName();
        this.address = new AddressResponseDto(studyCafe);
        this.usingSeats = usingSeats;
        this.seats = seats;
        this.sales = sales;
    }
}
