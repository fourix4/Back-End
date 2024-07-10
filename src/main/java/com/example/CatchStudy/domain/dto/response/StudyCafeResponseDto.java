package com.example.CatchStudy.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import java.time.LocalTime;
import java.util.List;

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
}
