package com.example.CatchStudy.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDto {

    private String city;
    private String country;
    private String town;
    private String etc;

    public String toAddress() {
        return this.city + " " + this.country + this.town + " " + this.etc;
    }
}
