package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.SeatBookingDto;
import com.example.CatchStudy.domain.dto.request.BookingRequestDto;
import com.example.CatchStudy.domain.dto.response.BookingResponseDto;
import com.example.CatchStudy.global.enums.SeatType;
import com.example.CatchStudy.service.BookingService;
import com.example.CatchStudy.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/studycafes")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final PaymentService paymentService;

    @PostMapping("/seats")
    public ResponseEntity<?> bookingSeats(@RequestBody BookingRequestDto bookingRequestDto){
        Long bookingId = null;
        SeatBookingDto dto = null;
        if(bookingRequestDto.getType()== SeatType.seat){
            dto = bookingRequestDto.toSeatDto();
            bookingId = bookingService.saveBooking(dto);
        }else if(bookingRequestDto.getType() == SeatType.room){
            dto = bookingRequestDto.toRoomDto();
            bookingId = bookingService.saveBooking(dto);
        }
        BookingResponseDto bookingResponseDto = paymentService.kakaoPayReady(dto,Long.valueOf(1),bookingId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingResponseDto);
    }

}
