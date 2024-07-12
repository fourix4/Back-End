package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.StudyCafe;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class StudyCafeResponseDto {

    private Long cafeId;
    private String cafeName;
    private String address;
    private List<String> cafeImages;
    private String seatingChartImage;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingHours;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closedHours;
    private String closedDay;
    private String cafePhone;
    private int totalSeats;
    private int availableSeats;
    private int totalRooms;
    private int availableRooms;

    public StudyCafeResponseDto(StudyCafe studyCafe, List<String> cafeImageUrls, String seatingChartImageUrl, int totalSeats, int availableSeats,
                                int totalRooms, int availableRooms) {
        this.cafeId = studyCafe.getCafeId();
        this.cafeName = studyCafe.getCafeName();
        this.address = studyCafe.getAddress();
        this.cafeImages = cafeImageUrls;
        this.seatingChartImage = seatingChartImageUrl;
        this.openingHours = studyCafe.getOpeningHours();
        this.closedHours = studyCafe.getClosedHours();
        this.closedDay = studyCafe.getClosedDay();
        this.cafePhone = studyCafe.getCafePhone();
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.totalRooms = totalRooms;
        this.availableRooms = availableRooms;
    }
}
