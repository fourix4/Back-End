package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.request.ChatRoomRequestDto;
import com.example.CatchStudy.domain.dto.request.MessageRequestDto;
import com.example.CatchStudy.domain.dto.response.ChatRoomResponseDto;
import com.example.CatchStudy.domain.dto.response.MessageResponseDto;
import com.example.CatchStudy.domain.entity.ChatRoom;
import com.example.CatchStudy.domain.entity.Message;
import com.example.CatchStudy.domain.entity.StudyCafe;
import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.ChatRoomRepository;
import com.example.CatchStudy.repository.MessageRepository;
import com.example.CatchStudy.repository.StudyCafeRepository;
import com.example.CatchStudy.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UsersRepository usersRepository;
    private final StudyCafeRepository studyCafeRepository;
    private final UsersService usersService;

    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {
        Users user = usersRepository.findByUserId(chatRoomRequestDto.getUserId()).
                orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        StudyCafe studyCafe = studyCafeRepository.findByCafeId(chatRoomRequestDto.getCafeId()).
                orElseThrow(() -> new CatchStudyException(ErrorCode.STUDYCAFE_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(user, studyCafe));

        return new ChatRoomResponseDto(chatRoom);
    }

    public List<ChatRoomResponseDto> getChatRoomList() {
        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
        long userId = usersService.getCurrentUserId();
        List<ChatRoom> chatRoomList = chatRoomRepository.findByUserId(userId);

        for(ChatRoom chatRoom : chatRoomList) {
            Message message = messageRepository.findTopByChatRoomIdOrderByCreateDateDesc(chatRoom.getChatRoomId()).
                    orElse(new Message());
            chatRoomResponseDtoList.add(new ChatRoomResponseDto(chatRoom, message));
        }

        return chatRoomResponseDtoList;
    }

    public List<MessageResponseDto> getMessageList(long chatRoomId) {
        return messageRepository.findByChatRoomId(chatRoomId).stream().map(MessageResponseDto::new).toList();
    }

    @Transactional
    public MessageResponseDto createMessage(long chatRoomId, MessageRequestDto messageRequestDto) {

        Users user = usersRepository.findByUserId(messageRequestDto.getUserId()).
                orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).
                orElseThrow(() -> new CatchStudyException(ErrorCode.CHATROOM_NOT_FOUND));

        return new MessageResponseDto(messageRepository.save(new Message(messageRequestDto, user, chatRoom)));
    }
}
