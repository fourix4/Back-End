package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.StudyCafeResponseDto;
import com.example.CatchStudy.service.StudyCafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/studycafes")
@RequiredArgsConstructor
public class StudyCafeController {

    private final StudyCafeService studyCafeService;

    @GetMapping("/{cafe_id}")
    public ResponseEntity<?> getStudyCafe(@RequestParam long cafeId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(studyCafeService.getStudyCafe(cafeId)));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchStudyCafes(@RequestParam(required = false) String city,
                                             @RequestParam(required = false) String country,
                                             @RequestParam(required = false) String town,
                                             @RequestParam(defaultValue = "1") int page) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(studyCafeService.searchStudyCafes(city, country, town, page)));
    }
}
