package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

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
}
