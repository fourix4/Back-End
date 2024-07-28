package com.example.CatchStudy.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsageFeeRequestDto {

    private int hours;
    private int price;
}
