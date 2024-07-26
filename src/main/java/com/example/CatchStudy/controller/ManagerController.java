package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.AddressRequestDto;
import com.example.CatchStudy.domain.dto.request.ManagerRequestDto;
import com.example.CatchStudy.domain.dto.request.RoomInfoRequestDto;
import com.example.CatchStudy.domain.dto.request.UsageFeeRequestDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/manager")
    public Response saveStudyCafe(@RequestParam String cafeName, @RequestParam AddressRequestDto address,
                                  @RequestParam String openingHours, @RequestParam String closedHours,
                                  @RequestParam String closedDay, @RequestParam String cafePhone,
                                  @RequestParam Integer seats, @RequestParam RoomInfoRequestDto roomInfo,
                                  @RequestParam List<UsageFeeRequestDto> usageFee, @RequestParam MultipartFile titleImage,
                                  @RequestParam List<MultipartFile> multipleImages, @RequestParam MultipartFile seatChartImage) {
        ManagerRequestDto managerRequestDto = new ManagerRequestDto(
                cafeName, address, openingHours, closedHours, closedDay, cafePhone, seats, roomInfo, usageFee, titleImage, multipleImages, seatChartImage);

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
