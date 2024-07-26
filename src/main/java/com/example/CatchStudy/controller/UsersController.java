package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.response.BookingHistoryByDateResponseDto;
import com.example.CatchStudy.domain.dto.response.BookingHistoryResponseDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.UsersRepository;
import com.example.CatchStudy.service.BookingService;

import com.example.CatchStudy.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

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

    @GetMapping("")
    public Response getUserInfo() {
        return Response.success(Result.toResponseDto(usersService.getUserInfo()));
    }

    @GetMapping("/check")
    public Response checkUser() {
        usersService.getCurrentUserId();
        return Response.success();
    }

    @PostMapping("/logout")
    public Response logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        usersService.logout(token);

        return Response.success();
    }

    @PostMapping("/reissuance")
    public Response reissuanceAccessToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        return Response.success(Result.toResponseDto(usersService.reissuanceAccessToken(token)));
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
