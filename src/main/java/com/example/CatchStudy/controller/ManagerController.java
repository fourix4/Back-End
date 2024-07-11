package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.StudyCafeRequestDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/manager")
    public ResponseEntity<?> saveStudyCafe(StudyCafeRequestDto studyCafeRequestDto) {
        managerService.saveStudyCafe(studyCafeRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success());
    }

    @PatchMapping("/manager")
    public ResponseEntity<?> updateStudyCafe(StudyCafeRequestDto studyCafeRequestDto) {
        managerService.updateStudyCafe(studyCafeRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success());
    }


}
