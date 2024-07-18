package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.request.MessageRequestDto;
import com.example.CatchStudy.domain.dto.response.MessageResponseDto;
import com.example.CatchStudy.domain.entity.ChatRoom;
import com.example.CatchStudy.domain.entity.Message;
import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.ChatRoomRepository;
import com.example.CatchStudy.repository.MessageRepository;
import com.example.CatchStudy.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MessageService {

    private final UsersRepository usersRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public MessageResponseDto createMessage(long chatRoomId, MessageRequestDto messageRequestDto) {

        Users user = usersRepository.findByUserId(messageRequestDto.getUserId()).
                orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).
                orElseThrow(() -> new CatchStudyException(ErrorCode.CHATROOM_NOT_FOUND));

        return new MessageResponseDto(messageRepository.save(new Message(messageRequestDto, user, chatRoom)));
    }
}
