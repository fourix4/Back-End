package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.response.StudyCafeResponseDto;
import com.example.CatchStudy.service.StudyCafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/studycafe")
@RequiredArgsConstructor
public class StudyCafeController {

    private final StudyCafeService studyCafeService;

    @GetMapping("/{cafe_id}")
    public StudyCafeResponseDto getStudyCafe() {

    }
}
