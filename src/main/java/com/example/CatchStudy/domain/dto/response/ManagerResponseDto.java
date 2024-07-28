package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.StudyCafe;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ManagerResponseDto {

    private String cafe_name;
    private AddressResponseDto address;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime opening_hours;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closed_hours;
    private String closed_day;
    private String cafe_phone;
    private Integer seats;
    private RoomInfoResponseDto room_info;
    private List<UsageFeeResponseDto> usage_fee;
    private String title_image;
    private List<String> multiple_images;
    private String seat_chart_image;

    public ManagerResponseDto(StudyCafe studyCafe, int seats, RoomInfoResponseDto roomInfo, List<UsageFeeResponseDto> usageFee,
                              String titleImage, List<String> multipleImages, String seatChartImage) {
        this.cafe_name = studyCafe.getCafeName();
        this.address = new AddressResponseDto(studyCafe);
        this.opening_hours = studyCafe.getOpeningHours();
        this.closed_hours = studyCafe.getClosedHours();
        this.closed_day = studyCafe.getClosedDay();
        this.cafe_phone = studyCafe.getCafePhone();
        this.seats = seats;
        this.room_info = roomInfo;
        this.usage_fee = usageFee;
        this.title_image = titleImage;
        this.multiple_images= multipleImages;
        this.seat_chart_image = seatChartImage;
    }
}
