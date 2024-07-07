package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        usersService.logout();

        return ResponseEntity.status(HttpStatus.OK)
                .body("success");
    }

    @PostMapping("/reissuance")
    public ResponseEntity<?> reissuanceAccessToken() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(usersService.reissuanceAccessToken());
    }
}
