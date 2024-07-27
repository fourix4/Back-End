package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.SeatBookingDto;
import com.example.CatchStudy.domain.dto.request.BookingRequestDto;
import com.example.CatchStudy.domain.dto.request.SeatCheckInRequestDto;
import com.example.CatchStudy.domain.dto.request.SeatCheckOutRequestDto;
import com.example.CatchStudy.domain.dto.response.*;
import com.example.CatchStudy.global.enums.SeatType;
import com.example.CatchStudy.repository.UsersRepository;
import com.example.CatchStudy.service.BookingService;
import com.example.CatchStudy.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final UsersRepository usersRepository;

    @PostMapping("/studycafes/seats")
    public Response<BookingResponseDto> bookingSeats(@RequestBody BookingRequestDto bookingRequestDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = usersRepository.findByEmail(user.getUsername()).getUserId();
        SeatBookingDto dto = null;
        System.out.println("bookingrequest====================================");
        System.out.println(bookingRequestDto.getSeat_id());
        System.out.println(bookingRequestDto.getType());
        System.out.println(bookingRequestDto.getTime());
        System.out.println("bookingrequest====================================");
        if (bookingRequestDto.getType().equals("seat")) {
            dto = bookingRequestDto.toSeatDto();
        } else if (bookingRequestDto.getType().equals("room")) {
            dto = bookingRequestDto.toRoomDto();
        }
        BookingResponseDto bookingResponseDto = paymentService.kakaoPayReady(dto, userId);

        return Response.success(Result.toResponseDto(bookingResponseDto));
    }

    @GetMapping("/studycafes/{cafe_id}/seatingchart")
    public Response<SeatingChartResponseDto> seatingChart(@PathVariable("cafe_id") Long cafeId) {
        return Response.success(Result.toResponseDto(bookingService.showSeatingChart(cafeId)));
    }

    @GetMapping("/booking")
    public Response<AvailableBookingResponseDto> AvailableBooking(Authentication authentication) { //현재 예약 내용 확인
        User user = (User) authentication.getPrincipal();
        Long userId = usersRepository.findByEmail(user.getUsername()).getUserId();
        return Response.success(Result.toResponseDto(bookingService.getAvailableBooking(userId)));
    }

    @PatchMapping("/booking/checkin") //입실 시간 등록
    public Response<Void> checkInSeat(@RequestBody SeatCheckInRequestDto dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = usersRepository.findByEmail(user.getUsername()).getUserId();
        bookingService.updateSeatStartTime(userId, dto.getCode());
        return Response.success();
    }

    @PatchMapping("/booking/checkout")
    public Response<Void> checkOutSeat(@RequestBody SeatCheckOutRequestDto dto) { // 퇴실
        System.out.println("bookingId: "+ dto.getBooking_id());
        bookingService.checkOutSeat(dto.getBooking_id());
        return Response.success();
    }


}
