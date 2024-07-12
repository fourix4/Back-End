package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.response.AvailableRoomTimeResponseDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studycafes")
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/timeinfo")
    public Response<AvailableRoomTimeResponseDto> bookingStatusByDate(@RequestParam("roomId") Long roomId, @RequestParam("date") String date, @RequestParam("time") Integer time) {
        List<String> times = roomService.getBookingStatusByDate(roomId, date, time);
        return Response.success(Result.toResponseDto(AvailableRoomTimeResponseDto.toResponseDto(times)));
    }
}
