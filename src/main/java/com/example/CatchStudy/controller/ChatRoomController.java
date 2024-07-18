package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.ChatRoomRequestDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("")
    public Response createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {

        return Response.success(Result.toResponseDto(chatRoomService.createChatRoom(chatRoomRequestDto)));
    }

    @GetMapping("/rooms")
    public Response getChatRoomList() {
        return Response.success(Result.toResponseDto(chatRoomService.getChatRoomList()));
    }

    @GetMapping("/{chat_room_id}")
    public Response getChatRoom(@PathVariable("chat_room_id") long chatRoomId) {
        return Response.success(Result.toResponseDto(chatRoomService.getMessageList(chatRoomId)));
    }

}
