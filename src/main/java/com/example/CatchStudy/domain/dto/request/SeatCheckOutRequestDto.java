package com.example.CatchStudy.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SeatCheckOutRequestDto {

    @JsonProperty("booking_id")
    private Long booking_id;

}
