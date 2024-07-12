package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.StudyCafe;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddressResponseDto {

    private String city;
    private String country;
    private String town;
    private String etc;

    public AddressResponseDto(StudyCafe studyCafe) {
        String[] addressComponents = studyCafe.getAddress().split(" ");

        this.city = addressComponents[0];
        this.country = addressComponents[1];
        this.town = addressComponents[2];
        this.etc = addressComponents[3];
    }
}
