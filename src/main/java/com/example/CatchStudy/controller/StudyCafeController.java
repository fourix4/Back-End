package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.domain.dto.response.StudyCafeResponseDto;
import com.example.CatchStudy.service.StudyCafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/studycafes")
@RequiredArgsConstructor
public class StudyCafeController {

    private final StudyCafeService studyCafeService;

    @GetMapping("/{cafeId}")
    public Response getStudyCafe(@PathVariable long cafeId) {
        return Response.success(Result.toResponseDto(studyCafeService.getStudyCafe(cafeId)));
    }

    @GetMapping("/search")
    public Response searchStudyCafes(@RequestParam(required = false) String city,
                                             @RequestParam(required = false) String country,
                                             @RequestParam(required = false) String town,
                                             @RequestParam(defaultValue = "1") int page) {

        return Response.success(Result.toResponseDto((studyCafeService.searchStudyCafes(city, country, town, page))));
    }
}
