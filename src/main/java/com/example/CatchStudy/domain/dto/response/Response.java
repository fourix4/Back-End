package com.example.CatchStudy.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private String code;
    private String message;
    private T result;
    public static Response<Void> error(String errorCode, String message) {
        return new Response<>(errorCode, message, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("200", "success", result);
    }

    public static Response<Void> success() {
        return new Response<Void>("200", "success", null);
    }
}
