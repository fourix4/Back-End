package com.example.CatchStudy.domain.dto;

import com.example.CatchStudy.domain.entity.StudyCafe;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StudyCafeDto {

    private Long cafeId;
    private UsersDto usersDto;
    private String cafeName;
    private String address;
    private String city;
    private String country;
    private String town;
    private LocalTime openingHours;
    private LocalTime closedHours;
    private String closedDay;
    private String cafePhone;

    public static StudyCafeDto from(StudyCafe entity) {
        return new StudyCafeDto(
                entity.getCafeId(),
                UsersDto.from(entity.getUser()),
                entity.getCafeName(),
                entity.getAddress(),
                entity.getCity(),
                entity.getCountry(),
                entity.getTown(),
                entity.getOpeningHours(),
                entity.getClosedHours(),
                entity.getClosedDay(),
                entity.getCafePhone()
        );
    }
}
