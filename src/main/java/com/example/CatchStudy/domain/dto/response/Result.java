package com.example.CatchStudy.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private T result;

    public static <T> Result<T> toResponseDto(T result) {
        return new Result<>(result);
    }
}
