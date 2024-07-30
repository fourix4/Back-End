package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.RoomBookingCancelRequestDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.service.BookingService;
import com.example.CatchStudy.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;

    @PatchMapping("/booking/rooms/cancel")
    public Response<Void> cancel(@RequestBody RoomBookingCancelRequestDto requestDto) { //스터디룸 예약 취소
        paymentService.kakaoCancel(requestDto.getBooking_id());
        return Response.success();
    }

    @GetMapping("/payment/cancel/{paymentId}") //결제 중 취소
    public void canceled(@PathVariable("paymentId") Long paymentId) {
        bookingService.deleteBooking(paymentId);
        throw new CatchStudyException(ErrorCode.PAYMENT_CANCELED);
    }

    @GetMapping("/payment/fail/{paymentId}") //결제 실패
    public void fail(@PathVariable("paymentId") Long paymentId) {
        bookingService.deleteBooking(paymentId);
        throw new CatchStudyException(ErrorCode.PAYMENT_FAIL);
    }

    @GetMapping("/payment/success/{userId}/{paymentId}")
    public void afterPayRequest(@RequestParam("pg_token") String pgToken, @PathVariable("userId") Long userId, @PathVariable("paymentId") Long paymentId, HttpServletResponse response) throws IOException { //결제 성공
        paymentService.kakaoPayApprove(pgToken, userId, paymentId);
        String redirectUrl = "http://localhost:3000/payment-success";
        response.sendRedirect(redirectUrl);
    }
}
