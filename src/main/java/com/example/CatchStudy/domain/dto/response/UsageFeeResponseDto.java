package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.UsageFee;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsageFeeResponseDto {

    private int hours;
    private int price;

    public UsageFeeResponseDto(UsageFee usageFee) {
        this.hours = usageFee.getHours();
        this.price = usageFee.getPrice();
    }
}
