package com.example.CatchStudy.global.exception;

import com.example.CatchStudy.domain.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(CatchStudyException.class)
    public ResponseEntity<?> applicationHandler(CatchStudyException e) {
        log.error("Error cause {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().getStatus().toString(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e) {
        log.error("Error cause {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error("500", ErrorCode.INTERNAL_SERVER_ERROR.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
