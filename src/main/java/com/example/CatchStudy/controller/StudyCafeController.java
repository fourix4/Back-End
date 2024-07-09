package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.StudyCafeRequestDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.service.StudyCafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudyCafeController {

    private final StudyCafeService studyCafeService;

    @PostMapping("/manager")
    public ResponseEntity<?> createStudyCafe(StudyCafeRequestDto studyCafeRequestDto) {
        studyCafeService.saveStudyCafe(studyCafeRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success());
    }
}
