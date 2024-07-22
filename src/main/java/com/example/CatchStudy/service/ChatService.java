package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.request.ChatRoomRequestDto;
import com.example.CatchStudy.domain.dto.request.MessageRequestDto;
import com.example.CatchStudy.domain.dto.response.ChatRoomResponseDto;
import com.example.CatchStudy.domain.dto.response.MessageResponseDto;
import com.example.CatchStudy.domain.entity.*;
import com.example.CatchStudy.global.enums.Author;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UsersRepository usersRepository;
    private final StudyCafeRepository studyCafeRepository;
    private final UsersService usersService;
    private final ChatNotificationRepository chatNotificationRepository;
    private final Map<Long, Map<String, Long>> chatRoomMap = new ConcurrentHashMap<>(); // chatRoomId, <session id, 접속한 유저 id>
    private final Map<String, Long> sessionToChatRoom = new ConcurrentHashMap<>();

    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {
        Users user = usersRepository.findByUserId(usersService.getCurrentUserId()).
                orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        StudyCafe studyCafe = studyCafeRepository.findByCafeId(chatRoomRequestDto.getCafeId()).
                orElseThrow(() -> new CatchStudyException(ErrorCode.STUDYCAFE_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(user, studyCafe));

        return new ChatRoomResponseDto(chatRoom);
    }

    public List<ChatRoomResponseDto> getChatRoomList() {
        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
        long userId = usersService.getCurrentUserId();
        Users user = usersRepository.findByUserId(userId).
                orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        List<ChatRoom> chatRoomList = chatRoomRepository.findByUserId(userId);

        for(ChatRoom chatRoom : chatRoomList) {
            Message message = messageRepository.findTopByChatRoomIdOrderByCreateDateDesc(chatRoom.getChatRoomId()).
                    orElse(new Message());
            ChatNotification chatNotification = chatNotificationRepository.findFirstByChatRoomAndUserOrderByChatNotificationIdDesc(chatRoom, user)
                    .orElse(null);
            boolean status = chatNotification == null || chatNotification.isStatus();   // null 인 경우 알림 x 이므로 true

            chatRoomResponseDtoList.add(new ChatRoomResponseDto(chatRoom, message, status));
        }

        return chatRoomResponseDtoList;
    }

    @Transactional
    public List<MessageResponseDto> getMessageList(long chatRoomId) {
        long userId = usersService.getCurrentUserId();
        List<ChatNotification> chatNotificationList = chatNotificationRepository.findAllByChatRoom_ChatRoomIdAndUser_UserId(chatRoomId, userId);
        // 알림 읽음 처리
        for(ChatNotification chatNotification : chatNotificationList) {
           chatNotification.readNotification();
        }

        return messageRepository.findByChatRoomId(chatRoomId).stream().map(MessageResponseDto::new).toList();
    }

    @Transactional
    public MessageResponseDto createMessage(long chatRoomId, MessageRequestDto messageRequestDto) {

        Users user = usersRepository.findByUserId(usersService.getCurrentUserId()).
                orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).
                orElseThrow(() -> new CatchStudyException(ErrorCode.CHATROOM_NOT_FOUND));

        int userCount = chatRoomMap.get(chatRoomId).size(); // 참여중인 채탕방의 유저 수 1 or 2

        if(userCount == 1) {
            if(user.getAuthor().equals(Author.roleUser)) {
                Users studyCafeUser = usersRepository.findByUserId(chatRoom.getStudyCafe().getUser().getUserId()).
                        orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
                chatNotificationRepository.save(new ChatNotification(chatRoom, studyCafeUser));
            } else {
                Users client =  chatRoom.getUser();
                chatNotificationRepository.save(new ChatNotification(chatRoom, client));
            }
        }

        return new MessageResponseDto(messageRepository.save(new Message(messageRequestDto, user, chatRoom)));
    }

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);
        System.out.println("-------- message : " + event.getMessage());
        System.out.println("-------- accessor : " + accessor.toString());

        // simpConnectMessage 헤더가 존재하는지 확인
        if (accessor.getHeader("simpConnectMessage") == null) {
            System.out.println("simpConnectMessage 헤더가 존재하지 않습니다.");
            return;
        }

        GenericMessage generic = (GenericMessage) accessor.getHeader("simpConnectMessage");

        if (generic == null) {
            System.out.println("generic 객체가 null입니다.");
            return;
        }

        Map nativeHeaders = (Map) generic.getHeaders().get("nativeHeaders");
        if (nativeHeaders == null) {
            System.out.println("nativeHeaders가 존재하지 않습니다.");
            return;
        }

        Long chatRoomId;
        try {
            chatRoomId = Long.parseLong((String) ((List) nativeHeaders.get("chatRoomId")).get(0));
        } catch (Exception e) {
            System.out.println("chatRoomId를 파싱하는 중 오류 발생: " + e.getMessage());
            return;
        }

        String sessionId = (String) generic.getHeaders().get("simpSessionId");
        if (sessionId == null) {
            System.out.println("sessionId가 존재하지 않습니다.");
            return;
        }

        long userId = usersService.getCurrentUserId();

        Map<String, Long> userMap = chatRoomMap.getOrDefault(chatRoomId, new HashMap<>());
        userMap.put(sessionId, userId);

        chatRoomMap.put(chatRoomId, userMap);
        sessionToChatRoom.put(sessionId, chatRoomId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        Long chatRoomId = sessionToChatRoom.get(sessionId);

        Map<String, Long> userMap = chatRoomMap.get(chatRoomId);
        userMap.remove(sessionId);
        sessionToChatRoom.remove(sessionId);

        if (userMap.isEmpty()) chatRoomMap.remove(chatRoomId);
        else chatRoomMap.put(chatRoomId, userMap);
    }
}
