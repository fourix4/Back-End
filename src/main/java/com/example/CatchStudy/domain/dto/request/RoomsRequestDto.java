package com.example.CatchStudy.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomsRequestDto {

    private String roomName;
    private int capacity;
    private int price;
}
