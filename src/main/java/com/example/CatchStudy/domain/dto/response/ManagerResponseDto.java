package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.StudyCafe;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ManagerResponseDto {

    private long cafeId;
    private String cafeName;
    private AddressResponseDto address;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingHours;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closedHours;
    private String closedDay;
    private String cafePhone;
    private Integer usingSeats;
    private Integer seats;
    private RoomInfoResponseDto roomInfo;
    private List<UsageFeeResponseDto> usageFee;
    private String titleImage;
    private List<String> multipleImages;
    private String seatChartImage;
    private Integer sales;

    public ManagerResponseDto(StudyCafe studyCafe, int usingSeats, int seats, RoomInfoResponseDto roomInfo, List<UsageFeeResponseDto> usageFee,
                              String titleImage, List<String> multipleImages, String seatChartImage, int sales) {

        this.cafeId = studyCafe.getCafeId();
        this.cafeName = studyCafe.getCafeName();
        this.address = new AddressResponseDto(studyCafe);
        this.openingHours = studyCafe.getOpeningHours();
        this.closedHours = studyCafe.getClosedHours();
        this.closedDay = studyCafe.getClosedDay();
        this.cafePhone = studyCafe.getCafePhone();
        this.usingSeats = usingSeats;
        this.seats = seats;
        this.roomInfo = roomInfo;
        this.usageFee = usageFee;
        this.titleImage = titleImage;
        this.multipleImages= multipleImages;
        this.seatChartImage = seatChartImage;
        this.sales = sales;
    }
}
