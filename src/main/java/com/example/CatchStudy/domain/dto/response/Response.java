package com.example.CatchStudy.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private int code;
    private String message;
    private Result data;
    public static Response<Void> error(String errorCode, String message) {
        return new Response<>(Integer.valueOf(errorCode), message, null);
    }
    public static Response success(Result data) {
        return new Response<>(200, "success", data);
    }

    public static Response<Void> success() {
        return new Response<Void>(200, "success", null);
    }

}
