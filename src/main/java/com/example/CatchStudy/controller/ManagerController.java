package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.ManagerRequestDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/manager")
    public Response saveStudyCafe(@RequestBody ManagerRequestDto managerRequestDto) {
        managerService.saveStudyCafe(managerRequestDto);
        System.out.println(managerRequestDto.getClosedDay());
        System.out.println(managerRequestDto.getCafeName());
        System.out.println(managerRequestDto.getSeats());
        System.out.println(managerRequestDto.getCafePhone());
        return Response.success();
    }

    @PatchMapping("/manager")
    public Response updateStudyCafe(@RequestBody ManagerRequestDto managerRequestDto) {
        managerService.updateStudyCafe(managerRequestDto);
        return Response.success();
    }

    @GetMapping("/manager")
    public Response getStudyCafe() {
        return Response.success(Result.toResponseDto(managerService.getStudyCafe()));
    }
}
