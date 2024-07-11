package com.example.CatchStudy.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class StudyCafeRequestDto {

    private String cafeName;
    private AddressRequestDto address;
    private String openingHours;
    private String closedHours;
    private String closedDay;
    private String cafePhone;
    private int seats;
    private RoomInfoRequestDto roomInfo;
    private List<UsageFeeRequestDto> usageFee;
    private MultipartFile titleImage;
    private List<MultipartFile> multipleImages;
    private MultipartFile seatChartImage;
}
