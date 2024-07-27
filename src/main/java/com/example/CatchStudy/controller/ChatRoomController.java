package com.example.CatchStudy.controller;

import com.example.CatchStudy.domain.dto.request.ChatRoomRequestDto;
import com.example.CatchStudy.domain.dto.response.Response;
import com.example.CatchStudy.domain.dto.response.Result;
import com.example.CatchStudy.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatService chatService;

    @PostMapping("")
    public Response createChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        System.out.println("cafeId ------------ : " + chatRoomRequestDto.getCafeId());
        return Response.success(Result.toResponseDto(chatService.createChatRoom(chatRoomRequestDto)));
    }

    @GetMapping("/rooms")
    public Response getChatRoomList() {
        return Response.success(Result.toResponseDto(chatService.getChatRoomList()));
    }

    @GetMapping("/{chat_room_id}")
    public Response getChatRoom(@PathVariable("chat_room_id") long chatRoomId) {
        return Response.success(Result.toResponseDto(chatService.getMessageList(chatRoomId)));
    }

}
