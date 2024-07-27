package com.example.CatchStudy.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SeatCheckOutRequestDto {

    @JsonProperty("booking_id")
    private Long booking_id;

}
