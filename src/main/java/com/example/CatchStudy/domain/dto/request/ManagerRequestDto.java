package com.example.CatchStudy.domain.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ManagerRequestDto {

    private String cafeName;
    private AddressRequestDto address;
    private String openingHours;
    private String closedHours;
    private String closedDay;
    private String cafePhone;
    private Integer seats;
    private RoomInfoRequestDto roomInfo;
    private List<UsageFeeRequestDto> usageFee;
    private MultipartFile titleImage;
    private List<MultipartFile> multipleImages;
    private MultipartFile seatChartImage;
}
