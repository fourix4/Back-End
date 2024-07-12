package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.response.BookingHistoryByDateResponseDto;
import com.example.CatchStudy.domain.dto.response.BookingHistoryResponseDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.repository.UsersRepository;
import com.example.CatchStudy.service.BookingService;

import com.example.CatchStudy.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final BookingService bookingService;
    private final UsersRepository usersRepository;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @PostMapping("/login/kakao")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoRedirectUri);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        usersService.logout(token);

        return ResponseEntity.status(HttpStatus.OK)
                .body("success");
    }

    @PostMapping("/reissuance")
    public ResponseEntity<?> reissuanceAccessToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.toResponseDto(usersService.reissuanceAccessToken(token)));
    }

    @GetMapping("/history")
    public Response<BookingHistoryResponseDto> getBooking(Authentication authentication){
        User user =  (User) authentication.getPrincipal();
        Long userId = usersRepository.findByEmail(user.getUsername()).getUserId();
        return Response.success(Result.toResponseDto(bookingService.getBookingHistory(userId)));
    }

    @GetMapping("/history/date")
    public Response<BookingHistoryByDateResponseDto> getBooking2(@RequestParam("start_date") LocalDate startDate, @RequestParam("end_date")LocalDate endDate, @RequestParam("page")Integer page, Authentication authentication){
        User user =  (User) authentication.getPrincipal();
        Long userId = usersRepository.findByEmail(user.getUsername()).getUserId();
        return Response.success(Result.toResponseDto(bookingService.getBookingHistoryByDate(userId,startDate,endDate,page)));
    }
}
