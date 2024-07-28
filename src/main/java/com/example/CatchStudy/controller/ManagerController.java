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
    public Response updateStudyCafe(@RequestParam(name = "cafe_name") String cafeName,
                                    @ModelAttribute(name = "address") AddressRequestDto address,
                                    @RequestParam(name = "opening_hours") String openingHours,
                                    @RequestParam(name = "closed_hours") String closedHours,
        @RequestParam(name = "closed_day") String closedDay,
        @RequestParam(name = "cafe_phone") String cafePhone,
        @RequestParam(name = "seats") Integer seats,
        @ModelAttribute(name = "room_info") RoomInfoRequestDto roomInfo,
        @ModelAttribute(name = "usage_fee") List<UsageFeeRequestDto> usageFee,
        @RequestParam(name = "title_image", required = false) MultipartFile titleImage,
        @RequestParam(name = "multiple_images", required = false) List<MultipartFile> multipleImages,
        @RequestParam(name = "seat_chart_image", required = false) MultipartFile seatChartImage) {
            ManagerRequestDto managerRequestDto = new ManagerRequestDto(
                    cafeName, address, openingHours, closedHours, closedDay, cafePhone, seats, roomInfo, usageFee, titleImage, multipleImages, seatChartImage);

        managerService.updateStudyCafe(managerRequestDto);
        return Response.success();
    }

    @GetMapping("/manager")
    public Response getStudyCafe() {
        return Response.success(Result.toResponseDto(managerService.getStudyCafe()));
    }
}
