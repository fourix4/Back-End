package com.example.CatchStudy.domain.dto;

import com.example.CatchStudy.domain.entity.UsageFee;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsageFeeDto {

    private Integer hours;
    private Integer price;

    public static UsageFeeDto from(UsageFee entity) {
        return new UsageFeeDto(
                entity.getHours(),
                entity.getPrice()
        );
    }
}
