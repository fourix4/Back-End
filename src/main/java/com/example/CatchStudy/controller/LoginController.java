package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.OauthCodeRequestDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    @PostMapping("/login/google")
    public Response googleLogin(@RequestBody OauthCodeRequestDto oauthCodeRequestDto) {
        return Response.success(Result.toResponseDto(loginService.googleLogin(oauthCodeRequestDto)));
    }
}
