package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.AddressRequestDto;
import com.example.CatchStudy.domain.dto.request.ManagerRequestDto;
import com.example.CatchStudy.domain.dto.request.RoomInfoRequestDto;
import com.example.CatchStudy.domain.dto.request.UsageFeeRequestDto;
import com.example.CatchStudy.domain.dto.response.ManagerResponseDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/manager")
    public Response saveStudyCafe(@RequestPart(value = "data") ManagerRequestDto managerRequestDto,
                                  @RequestPart(value = "title_image", required = false) MultipartFile titleImage,
                                  @RequestPart(value = "multiple_images", required = false) List<MultipartFile> multipleImages) {

        managerService.saveStudyCafe(managerRequestDto, titleImage, multipleImages);

        return Response.success();
    }

    @PatchMapping("/manager/{cafe_id}")
    public Response updateStudyCafe(@PathVariable("cafe_id") long cafeId,
                                    @RequestPart(value = "data") ManagerRequestDto managerRequestDto,
                                    @RequestPart(value = "title_image", required = false) MultipartFile titleImage,
                                    @RequestPart(value = "multiple_images", required = false) List<MultipartFile> multipleImages) {

        managerService.updateStudyCafe(cafeId, managerRequestDto, titleImage, multipleImages);
        return Response.success();
    }

    @GetMapping("/manager")
    public Response getStudyCafeList() {
        return Response.success(Result.toResponseDto(managerService.getStudyCafeList()));
    }


    @GetMapping("/manager/{cafe_id}")
    public Response getStudyCafe(@PathVariable("cafe_id") long cafeId ) {
        return Response.success(Result.toResponseDto(managerService.getStudyCafe(cafeId)));
    }
}
