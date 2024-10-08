package com.example.CatchStudy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CatchStudyException extends RuntimeException {

    private ErrorCode errorCode;

    private String message;

    public CatchStudyException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        }
        return String.format("%s", "%s", errorCode.getMessage(), message);
    }
}
