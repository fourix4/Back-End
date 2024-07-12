package com.example.CatchStudy.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsageFeeRequestDto {

    private int hours;
    private int price;
}
