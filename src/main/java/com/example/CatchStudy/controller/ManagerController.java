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

    @PatchMapping("/manager")
    public Response updateStudyCafe(@RequestPart(value = "data") ManagerRequestDto managerRequestDto,
                                    @RequestPart(value = "title_image", required = false) MultipartFile titleImage,
                                    @RequestPart(value = "multiple_images", required = false) List<MultipartFile> multipleImages) {

        for(MultipartFile image : multipleImages) {
            if (image != null && !image.isEmpty()) {
                System.out.println("Title Image Name: " + image.getOriginalFilename());
                System.out.println("Title Image Size: " + image.getSize());
            } else {
                System.out.println("No Title Image Provided");
            }
        }

        managerService.updateStudyCafe(managerRequestDto, titleImage, multipleImages);
        return Response.success();
    }

    @GetMapping("/manager")
    public Response getStudyCafe() {
        return Response.success(Result.toResponseDto(managerService.getStudyCafe()));
    }
}
